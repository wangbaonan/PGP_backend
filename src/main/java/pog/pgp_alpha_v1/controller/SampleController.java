package pog.pgp_alpha_v1.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pog.pgp_alpha_v1.common.BaseResponse;
import pog.pgp_alpha_v1.common.ResultUtils;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.model.request.AddSamplesRequest;
import pog.pgp_alpha_v1.model.request.SampleInfoRequest;
import pog.pgp_alpha_v1.service.AnalysisSamplesService;
import pog.pgp_alpha_v1.service.SampleDataService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static pog.pgp_alpha_v1.constants.Constants.USER_LOGIN_STATE;

@RestController
@RequestMapping(value = "/sample")
public class SampleController {
    @Resource
    SampleDataService sampleDataService;
    @Resource
    AnalysisSamplesService analysisSamplesService;

    /**
     * 添加样本
     * @param request 样本id列表以及对应分析的请求体
     * @throws IOException 文件读写异常
     */
    @PostMapping("/add")
    public BaseResponse addSamples(@RequestBody AddSamplesRequest request,HttpServletRequest httpServletRequest) throws IOException {
        User currentUser = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(analysisSamplesService.addSamples(request.getAnalysisId(), request.getDataIds(), currentUser));
    }

    /**
     * 获取用户的样本列表
     * @param request 从Session中获取当前用户信息
     * @return 返回数据ID数组
     */
    @PostMapping("/list")
    public BaseResponse getSamplesList(HttpServletRequest request){
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(sampleDataService.getSampleDataByUser(currentUser));
    }

    /**
     * 获取样本信息
     * @param sampleInfoRequest 样本ID信息
     * @param request 获取用户信息
     * @return 返回数据基本信息
     */
    @PostMapping("/info")
    public BaseResponse getSamplesInfo(@RequestBody SampleInfoRequest sampleInfoRequest, HttpServletRequest request){
        Long dataId = sampleInfoRequest.getDataId();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(sampleDataService.getSampleData(dataId, currentUser));
    }
}
