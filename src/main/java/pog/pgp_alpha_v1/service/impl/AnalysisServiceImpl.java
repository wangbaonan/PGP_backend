package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.events.AnalysisProgressEvent;
import pog.pgp_alpha_v1.handler.ProcessHandler;
import pog.pgp_alpha_v1.mapper.AnalysisMapper;
import pog.pgp_alpha_v1.model.Analysis;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.service.AnalysisService;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static pog.pgp_alpha_v1.utils.FileUtils.createDirectoriesRecursively;

/**
 * @author 86183
 * @description 针对表【analysis】的数据库操作Service实现
 * @createDate 2023-04-19 15:18:15
 */
@Service
public class AnalysisServiceImpl extends ServiceImpl<AnalysisMapper, Analysis>
        implements AnalysisService {

    @Value("${my-app.analysis.analysisPath}")
    private String analysisAllPath;
    @Value("${my-app.analysis.cppPath}")
    private String cppPath;
    @Resource
    private ProcessHandler processHandler;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    public Long createAnalysis(User user) {
        if (user != null) {
            Analysis analysis = new Analysis();
            analysis.setUserId((user).getId());
            this.save(analysis);
            // 创建User对应的文件夹以及分析ID对应的文件夹
            Long analysisId = analysis.getAnalysisId();
            // Linux环境下路径可能需要换符号 使用Path来实现跨系统的路径拼接
            Path analysisPath = Paths.get(analysisAllPath, analysisId.toString());
            // 对应分析ID的路径已经构建完成
            createDirectoriesRecursively(analysisPath.toString());
            return analysis.getAnalysisId();
        }
        return null;
    }

    @Override
    public ArrayList<Analysis> getAnalysisList(User user) {
        if (user != null) {
            // 使用QueryWrapper来实现基于用户ID查询分析列表的方法
            QueryWrapper<Analysis> queryWrapper = new QueryWrapper<>();
            Long userId = user.getId();
            queryWrapper.eq("userId", userId);
            ArrayList<Analysis> analysisList = (ArrayList<Analysis>) this.list(queryWrapper);
            return analysisList;
        }
        return null;
    }

    @Override
    public boolean deleteAnalysis(Long analysisId, Long userId) {
        if (analysisId != null && userId != null) {
            QueryWrapper<Analysis> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("analysisId", analysisId);
            queryWrapper.eq("userId", userId);
            return this.remove(queryWrapper);
        }
        return false;
    }

    /**
     * 根据分析ID和用户ID来运行分析
     *
     * @param analysisId 分析ID
     * @param userId     用户ID
     * @return 运行成功返回true，否则返回false
     */
    @Override
    public boolean runAnalysis(Long analysisId, Long userId) {
        if (analysisId != null && userId != null) {
            // 使用QueryWrapper来实现基于用户ID查询分析列表的方法
            QueryWrapper<Analysis> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("analysisId", analysisId);
            queryWrapper.eq("userId", userId);
            Analysis analysis = this.getOne(queryWrapper);
            // 创建对应的output文件夹
            try {
                Path outputPath = Paths.get(analysisAllPath, analysisId.toString(), "output");
                createDirectoriesRecursively(outputPath.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Path configPath = Paths.get(analysisAllPath, analysisId.toString(), "config", "config.txt");
            Path inputPath = Paths.get(analysisAllPath, analysisId.toString(), "input");
            Path outputPath = Paths.get(analysisAllPath, analysisId.toString(), "output");
            String runCommand = cppPath + " --config " + configPath + " --input " + inputPath + " --output " + outputPath;
            // 如果查询到了对应的分析
            if (analysis != null) {
                // 设置分析状态为1，表示正在运行 0表示未运行 2表示运行完成
                analysis.setAnalysisStatus(1);
                this.updateById(analysis);
            }
            // 开始运行分析
            if (startProcess(runCommand, analysis)) {
                // 在运行分析的同时，监控PGP.process文件
                monitorPGPProcessFile(outputPath, userId, analysisId);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    @Async
    public boolean startProcess(String command, Analysis analysis) {
        // 创建ProcessBuilder来运行分析
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        // 开始运行分析
        try {
            Process process = processBuilder.start();
            process.waitFor();
            // 运行完成后设置分析状态为2
            analysis.setAnalysisStatus(2);
            this.updateById(analysis);
            return true;
        } catch (Exception e) {
            // 运行失败后设置分析状态为3
            analysis.setAnalysisStatus(3);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Async
    public void monitorPGPProcessFile(Path outputPath, Long userId, Long analysisId) {
        try {
            Path processFile = outputPath.resolve("PGP.process");
            WatchService watchService = FileSystems.getDefault().newWatchService();
            processFile.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            boolean running = true;
            while (running) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path modifiedFile = (Path) event.context();
                        if (modifiedFile.equals(processFile.getFileName())) {
                            List<String> lines = Files.readAllLines(processFile);
                            for (String line : lines) {
                                if (line.startsWith("[PROCESS]:")) {
                                    eventPublisher.publishEvent(new AnalysisProgressEvent(this, userId, analysisId, line));
                                }
                            }
                        }
                    }
                }
                running = key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}




