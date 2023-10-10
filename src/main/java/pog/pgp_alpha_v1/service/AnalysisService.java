package pog.pgp_alpha_v1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;
import pog.pgp_alpha_v1.model.Analysis;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.model.message.AnalysisProgressInfo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
* @author 86183
* @description 针对表【analysis】的数据库操作Service
* @createDate 2023-04-19 15:18:15
*/
public interface AnalysisService extends IService<Analysis> {

    /**
     * 根据用户创建一个分析的对象不需要传入参数通过Session获取当前登录的用户
     * @param user 用户
     * @return 分析对象的ID
     */
    Long createAnalysis(User user);

    /**
     * 根据分析ID获取分析状态
     * @param analysisId 分析ID
     * @param user 用户
     * @return 分析状态
     */
    Integer getAnalysisStatus(User user,Long analysisId);

    /**
     * 获取用户的多个分析，由于是多个分析，所以返回的是一个List
     * @param user 用户
     * @return 分析列表
     */
    ArrayList<Analysis> getAnalysisList(User user);

    /**
     * 获取分析的进度信息
     * @param analysisId 分析ID
     * @return 分析进度信息
     */
    AnalysisProgressInfo getAnalysisProgressInfo(Long analysisId);

    /**
     * 删除分析
     * @param analysisId 分析ID
     * @return 是否删除成功
     */
    boolean deleteAnalysis(Long analysisId, Long userId);

    /**
     * 运行分析
     * @param analysisId 分析ID
     * @return 是否运行成功
     */
    @Async
    CompletableFuture<Boolean> runAnalysis(Long analysisId, User currentUser, int moduleSwitchCode);

    /**
     * 启动分析
     * @param processBuilder 进程构建器
     * @param analysis 分析
     * @return 是否启动成功
     */
    @Async
    CompletableFuture<Boolean> startProcess(ProcessBuilder processBuilder, Analysis analysis);

    @Async
    void monitorPGPProcessFile(Path outputPath, Long userId, Long analysisId);

    @Async
    void waitForProcessAndUpdateStatus(Long analysisId, Analysis analysis, User currentUser, int moduleSwitchCode);
    void updateAnalysisStatus(Long analysisId, Integer status);
    void addAnalysisStatus(Long analysisId, Integer addStatus);
    void initialAnalysisStatus(Long analysisId);
    int getModuleSwitchCode(String moduleName);
}
