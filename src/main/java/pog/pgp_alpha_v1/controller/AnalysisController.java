package pog.pgp_alpha_v1.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pog.pgp_alpha_v1.common.BaseResponse;
import pog.pgp_alpha_v1.common.ResultUtils;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.model.request.AnalysisConfigRequest;
import pog.pgp_alpha_v1.model.request.AnalysisResultGetRequest;
import pog.pgp_alpha_v1.model.request.DeleteAnalysisSampleRequest;
import pog.pgp_alpha_v1.service.AnalysisResultService;
import pog.pgp_alpha_v1.service.AnalysisSamplesService;
import pog.pgp_alpha_v1.service.AnalysisService;
import pog.pgp_alpha_v1.service.ConfigService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static pog.pgp_alpha_v1.constants.Constants.USER_LOGIN_STATE;

@RestController
@RequestMapping(value = "/analysis")
public class AnalysisController {

    @Resource
    AnalysisService analysisService;

    @Resource
    AnalysisSamplesService analysisSamplesService;
    @Resource
    ConfigService configService;
    @Resource
    AnalysisResultService analysisResultService;

    /**
     * 创建分析
     * @param request 用于获取当前用户
     * @return 分析id
     */
    @PostMapping("/create")
    public BaseResponse createAnalysis(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisService.createAnalysis(currentUser));
    }

    /**
     * 运行分析
     * @param analysisConfigRequest 分析配置
     * @param request 用于获取当前用户
     * @return 运行结果
     */
    @PostMapping("/run")
    public BaseResponse runAnalysis(@RequestBody AnalysisConfigRequest analysisConfigRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        configService.updateConfig(analysisConfigRequest, analysisConfigRequest.getAnalysisId());
        int moduleSwitchCode = analysisConfigRequest.getModuleSwitchCode();
        return ResultUtils.success(analysisService.runAnalysis(analysisConfigRequest.getAnalysisId(), currentUser, moduleSwitchCode));
    }

    /**
     * 删除指定分析
     * @param analysisId 分析id
     * @param request 用于获取当前用户
     * @return 删除结果
     */
    @PostMapping("/delete")
    public BaseResponse deleteAnalysis(@RequestBody Long analysisId, HttpServletRequest request){
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        Long userId = currentUser.getId();
        // 同时传入analysisId和userId，防止用户删除其他用户的分析
        return ResultUtils.success(analysisService.deleteAnalysis(analysisId, userId));
    }

    /**
     * 获取分析列表
     * @param request 用于获取当前用户
     * @return 分析列表
     */
    @PostMapping("/list")
    public BaseResponse listAnalysis(HttpServletRequest request){
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisService.getAnalysisList(currentUser));
    }

    /**
     * 获取指定分析中的样本信息
     * @param analysisId 分析id
     * @param request 用于获取当前用户
     * @return 样本信息
     */
    @PostMapping("/sample")
    public BaseResponse getSample(@RequestBody Long analysisId, HttpServletRequest request){
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisSamplesService.getDataIds(analysisId, currentUser));
    }

    /**
     * 删除指定分析中的样本
     * @param request 用于获取当前用户
     * @return
     */
    @PostMapping("/sample/delete")
    public BaseResponse deleteSample(@RequestBody DeleteAnalysisSampleRequest deleteAnalysisSampleRequest, HttpServletRequest request){
        Long analysisId = deleteAnalysisSampleRequest.getAnalysisId();
        Long[] dataIds = deleteAnalysisSampleRequest.getDataIds();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisSamplesService.removeSamples(analysisId, dataIds, currentUser));
    }

    // 先检查用户与分析ID是否匹配，再查询结果路径
    @PostMapping("/result/prs")
    public BaseResponse getPrsResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getPrsResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/ancestry")
    public BaseResponse getAncestryResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getAncestryResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/similarity")
    public BaseResponse getSimilarityResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getSimilarityResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/archaicSegment")
    public BaseResponse getArchaicSegmentResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getArchaicSegResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/archaicSummary")
    public BaseResponse getArchaicSummaryResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getArchaicSumResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/davidChartReport")
    public BaseResponse getDavidChartReportResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getDavidChartReportResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/davidGeneClusterReport")
    public BaseResponse getDavidGeneClusterReportResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getDavidGeneClusterReportResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/davidTermClusterReport")
    public BaseResponse getDavidTermClusterReportResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getDavidTermClusterReportResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/archaicPlot")
    public BaseResponse getArchaicPlotResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getArchaicPlotResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/pca")
    public BaseResponse getPcaResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getPcaResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/sv")
    public BaseResponse getSvResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getSvResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/hla")
    public BaseResponse getHlaResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getHlaResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/prsHtml")
    public BaseResponse getPrsHtmlResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getPrsHtmlResultPath(analysisId, sampleId));
    }

    @PostMapping("/result/province")
    public BaseResponse getProvinceResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getProvinceResultPath(analysisId, sampleId));
    }

    @PostMapping("result/mty")
    public BaseResponse getMtyResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getMtyResultPath(analysisId, sampleId));
    }

    @PostMapping("result/snpediaMedicine")
    public BaseResponse getSnpediaMedicineResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getSnpediaMedicineResultPath(analysisId, sampleId));
    }

    @PostMapping("result/snpediaMedicalConditional")
    public BaseResponse getSnpediaMedicalConditionalResult(@RequestBody AnalysisResultGetRequest analysisResultGetRequest, HttpServletRequest request){
        Long analysisId = analysisResultGetRequest.getAnalysisId();
        String sampleId = analysisResultGetRequest.getSampleId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        return ResultUtils.success(analysisResultService.getSnpediaMedicalCondictionResultPath(analysisId, sampleId));
    }
}
