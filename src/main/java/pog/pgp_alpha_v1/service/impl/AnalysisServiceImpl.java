package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.events.AnalysisProgressEvent;
import pog.pgp_alpha_v1.mapper.AnalysisMapper;
import pog.pgp_alpha_v1.model.Analysis;
import pog.pgp_alpha_v1.model.SampleData;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.service.AnalysisResultService;
import pog.pgp_alpha_v1.service.AnalysisSamplesService;
import pog.pgp_alpha_v1.service.AnalysisService;
import pog.pgp_alpha_v1.service.SampleDataService;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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
    private ConcurrentHashMap<Long, Process> analysisProcessMap = new ConcurrentHashMap<>();
    @Value("${my-app.analysis.cppPath}")
    private String cppPath;
    private static final Logger logger = LoggerFactory.getLogger(AnalysisService.class);
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Resource
    private AnalysisResultService analysisResultService;
    private AnalysisSamplesService analysisSamplesService;
    @Resource
    private SampleDataService sampleDataService;

    @Autowired
    public void setAnalysisSamplesService(AnalysisSamplesService analysisSamplesService) {
        this.analysisSamplesService = analysisSamplesService;
    }

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
            return (ArrayList<Analysis>) this.list(queryWrapper);
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
     * @param analysisId  分析ID
     * @param currentUser 用户
     * @return 是否成功运行, 成功返回true, 失败返回false 并不代表分析已运行成功
     */
    @Override
    @Async
    public CompletableFuture<Boolean> runAnalysis(Long analysisId, User currentUser, int moduleSwitchCode) {
        Long userId = currentUser.getId();
        if (analysisId != null && userId != null) {
            QueryWrapper<Analysis> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("analysisId", analysisId);
            queryWrapper.eq("userId", userId);
            Analysis analysis = this.getOne(queryWrapper);
            //查询Status如果不为101则不允许运行
            if(analysis != null && analysis.getAnalysisStatus() != 101){
                return CompletableFuture.completedFuture(false);
            }
            try {
                Path outputPath = Paths.get(analysisAllPath, analysisId.toString(), "output");
                createDirectoriesRecursively(outputPath.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Path configPath = Paths.get(analysisAllPath, analysisId.toString(), "config", "config.txt");
            Path inputPath = Paths.get(analysisAllPath, analysisId.toString(), "input");
            Path outputPath = Paths.get(analysisAllPath, analysisId.toString(), "output");
            ProcessBuilder processBuilder = new ProcessBuilder(
                    cppPath,
                    "--config",
                    configPath.toString(),
                    "--indir",
                    inputPath.toString(),
                    "--outdir",
                    outputPath.toString());

            // 记录命令行log
            List<String> commandList = processBuilder.command();
            String commandString = String.join(" ", commandList);
            logger.info("[Command]: {}", commandString);
            // 如果查询到了对应的分析
            if (analysis != null) {
                // 设置分析状态为1，表示正在运行 0表示未运行 2表示运行完成
                analysis.setAnalysisStatus(1);
                this.updateById(analysis);
            }

            // 开始运行分析
            CompletableFuture<Boolean> analysisFuture = startProcess(processBuilder, analysis);
            // 在运行分析的同时，监控PGP.process文件 为了防止监控逃逸不使用thenAcceptAsync，而是选择直接异步启动监控任务
            CompletableFuture<Void> monitorFuture = CompletableFuture.runAsync(() -> monitorPGPProcessFile(outputPath, userId, analysisId));
            // 等待分析启动完成，然后更新分析状态 这里仅仅是启动完成，不代表分析完成
            CompletableFuture<Void> waitForProcessFuture = analysisFuture.thenAcceptAsync(result -> {
                waitForProcessAndUpdateStatus(analysisId, analysis, currentUser, moduleSwitchCode);
            });

            // 等待所有异步任务完成，然后返回analysisFuture的结果
            return CompletableFuture.allOf(analysisFuture, monitorFuture, waitForProcessFuture)
                    .thenApplyAsync(v -> analysisFuture.join());
        }
        return CompletableFuture.completedFuture(false);
    }


    @Override
    @Async
    public CompletableFuture<Boolean> startProcess(ProcessBuilder processBuilder, Analysis analysis) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Path outputLogPath = Paths.get(analysisAllPath, analysis.getAnalysisId().toString(), "output", "std.log");
                Path errorLogPath = Paths.get(analysisAllPath, analysis.getAnalysisId().toString(), "output", "err.log");
                processBuilder.redirectOutput(outputLogPath.toFile());
                processBuilder.redirectError(errorLogPath.toFile());
                Process process = processBuilder.start();
                // 将Process实例添加到ConcurrentHashMap中
                analysisProcessMap.put(analysis.getAnalysisId(), process);
                return true;
            } catch (IOException e) {
                logger.error("[AnalysisService.startProcess] Error occurred while running analysis.", e);
                return false;
            }
        });
    }

    @Async
    @Override
    public void waitForProcessAndUpdateStatus(Long analysisId, Analysis analysis, User currentUser, int moduleSwitchCode) {
        Process process = analysisProcessMap.get(analysisId);
        try {
            logger.info("[AnalysisService.waitForProcessAndUpdateStatus] Waiting for analysis process to finish.");
            process.waitFor();
            // 运行完成后设置分析状态为2
            logger.info("[AnalysisService.waitForProcessAndUpdateStatus] Analysis ID:[{}] process finished.", analysisId);
            analysis.setAnalysisStatus(2);
            this.updateById(analysis);
            // 遍历分析目录下的所有样本ID并将分析结果路径写入analysis_result表中
            Long[] dataIds = analysisSamplesService.getDataIds(analysisId, currentUser);
            logger.info("[Analysis total data Num]:{}", dataIds.length);
            // 利用dataId从sample表中查询出对应的样本名
            for (Long dataId : dataIds) {
                logger.info("[AnalysisService.waitForProcessAndUpdateStatus] Updating analysis result path for data ID:[{}]", dataId);
                SampleData sampleData = sampleDataService.getSampleData(dataId, currentUser);
                analysisResultService.updateAllResultPath(analysisId, sampleData.getSampleId(), moduleSwitchCode);
            }
            // analysisResultService.updateAllResultPath(analysisId, analysis, moduleSwitchCode);

        } catch (InterruptedException e) {
            // 运行失败后设置分析状态为3
            analysis.setAnalysisStatus(3);
            this.updateById(analysis);
            e.printStackTrace();
        }
    }

    @Override
    public void updateAnalysisStatus(Long analysisId, Integer status) {
        Analysis analysis = this.getById(analysisId);
        analysis.setAnalysisStatus(status); // 索引已建好可以开始分析
    }


    @Override
    @Async
    public void monitorPGPProcessFile(Path outputPath, Long userId, Long analysisId) {
        logger.info("[AnalysisService.monitorPGPProcessFile] Method called.");
        try {
            Path processFile = outputPath.resolve("PGP.process");
            WatchService watchService = FileSystems.getDefault().newWatchService();
            processFile.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            logger.info("[AnalysisService.monitorPGPProcessFile] Watching path: {}", processFile.getParent());
            boolean running = true;
            String lastLine = null;

            while (running) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path modifiedFile = (Path) event.context();
                        //logger.info("[AnalysisService.monitorPGPProcessFile] File modification event detected: {}", modifiedFile);
                        if (modifiedFile.equals(processFile.getFileName())) {
                            List<String> lines = Files.readAllLines(processFile);
                            if (!lines.isEmpty()) {
                                String newLastLine = lines.get(lines.size() - 1);
                                if (lastLine == null || !newLastLine.equals(lastLine)) {
                                    lastLine = newLastLine;
                                    if (lastLine.startsWith("[PROCESS]:")) {
                                        logger.info("[AnalysisService.monitorPGPProcessFile] : {}", lastLine);
                                        //logger.info("[AnalysisService.monitorPGPProcessFile] File modified: {}", modifiedFile);
                                        eventPublisher.publishEvent(new AnalysisProgressEvent(this, userId, analysisId, lastLine));
                                    }
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




