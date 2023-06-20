package pog.pgp_alpha_v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pog.pgp_alpha_v1.common.BaseResponse;
import pog.pgp_alpha_v1.common.ResultUtils;
import pog.pgp_alpha_v1.model.request.MultipartFileRequest;
import pog.pgp_alpha_v1.service.SampleDataService;
import pog.pgp_alpha_v1.utils.UploadUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static pog.pgp_alpha_v1.utils.UploadUtils.getCurrentUserId;
import static pog.pgp_alpha_v1.utils.UploadUtils.mergeFile;

@RestController
@RequestMapping(value = "/file/snp")
public class BigFileController {
    @Autowired
    private SampleDataService sampleDataService;
    @Value("${my-app.upload.uploadPath}")
    private String fileStorePath;
    private static final Logger logger = LoggerFactory.getLogger(BigFileController.class);

    @PostMapping(value = "/check")
    public BaseResponse checkBigFile(String fileMd5) {
        return UploadUtils.checkMd5(fileMd5,fileStorePath);
    }

    @PostMapping(value = "/upload")
    public BaseResponse uploadBigFile(@ModelAttribute MultipartFileRequest multipartFileRequest) {
        return UploadUtils.uploadFile(multipartFileRequest,fileStorePath);
    }

    // TODO 测试 添加了用户登录校验并添加到数据库中后的controller DONE
    @PostMapping(value = "/merge")
    public BaseResponse uploadFileMerge(HttpServletRequest request) {
        // 获取参数
        String fileMd5 = request.getParameter("fileMd5");
        String fileName = request.getParameter("fileName");
        String fileExt = request.getParameter("fileExt");
        File mergeFileDir = mergeFile(fileMd5, fileName, fileExt, fileStorePath);
        Long userId =  getCurrentUserId(request);
        // 保存上传文件信息，保存文件路径，即文件信息与用户信息关联起来了
        // 如果md5已经存在，就不再保存
        // 返回保存的数据id
        Long dataId = sampleDataService.saveUploadFile(mergeFileDir.getPath(), fileName, userId, fileMd5);
        if (dataId == null) {
            logger.warn("文件已存在于数据库中，不再保存");
        }
        return ResultUtils.success(dataId);
    }
}
