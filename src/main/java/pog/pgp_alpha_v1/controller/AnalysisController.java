package pog.pgp_alpha_v1.controller;

import org.springframework.web.bind.annotation.*;
import pog.pgp_alpha_v1.common.BaseResponse;
import pog.pgp_alpha_v1.common.ResultUtils;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.model.request.DeleteAnalysisSampleRequest;
import pog.pgp_alpha_v1.service.AnalysisSamplesService;
import pog.pgp_alpha_v1.service.AnalysisService;

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
}
