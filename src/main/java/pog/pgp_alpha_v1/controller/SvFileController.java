package pog.pgp_alpha_v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pog.pgp_alpha_v1.common.BaseResponse;
import pog.pgp_alpha_v1.common.ResultUtils;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.model.request.MultipartFileRequest;
import pog.pgp_alpha_v1.service.SvDataService;
import pog.pgp_alpha_v1.utils.UploadUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static pog.pgp_alpha_v1.constants.Constants.USER_LOGIN_STATE;
import static pog.pgp_alpha_v1.utils.UploadUtils.getCurrentUserId;
import static pog.pgp_alpha_v1.utils.UploadUtils.mergeFile;

@RestController
@RequestMapping(value = "/file/sv")
public class SvFileController {
    @Autowired
    private SvDataService svDataService;
    @Value("${my-app.upload.svUploadPath}")
    private String fileStorePath;

    @PostMapping(value = "/check")
    public BaseResponse checkBigFile(String fileMd5) {
        return UploadUtils.checkMd5(fileMd5,fileStorePath);
    }

    @PostMapping(value = "/upload")
    public BaseResponse uploadBigFile(@ModelAttribute MultipartFileRequest multipartFileRequest) {
        return UploadUtils.uploadFile(multipartFileRequest,fileStorePath);
    }

    @PostMapping(value = "/merge")
    public BaseResponse uploadFileMerge(HttpServletRequest request) {
        // 获取参数
        Long dataId = Long.parseLong(request.getParameter("dataId"));
        String fileMd5 = request.getParameter("fileMd5");
        String fileName = request.getParameter("fileName");
        String fileExt = request.getParameter("fileExt");
        String sampleId = request.getParameter("sampleId");
        File mergeFileDir = mergeFile(fileMd5, fileName, fileExt, fileStorePath);
        // 将上传合并后的文件路径添加到数据库中
        Long userId =  getCurrentUserId(request);
        // 保存上传文件信息，保存文件路径，即文件信息与用户信息关联起来了
        // 如果md5已经存在，就不再保存
        svDataService.saveUploadFile(dataId, mergeFileDir.getPath(), fileName, sampleId, userId, fileMd5);
        return ResultUtils.success(dataId);
    }

    //获取文件列表
    @PostMapping(value = "/list")
    public BaseResponse getFileList(HttpServletRequest httpServletRequest) {
        User currentUser = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(svDataService.getSvDataList(currentUser));
    }

}
