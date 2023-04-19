package pog.pgp_alpha_v1.controller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pog.pgp_alpha_v1.common.BaseResponse;
import pog.pgp_alpha_v1.common.ResultUtils;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.model.request.MultipartFileRequest;
import pog.pgp_alpha_v1.service.SampleDataService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import static pog.pgp_alpha_v1.common.ErrorCode.FILE_UPLOAD_ERROR;
import static pog.pgp_alpha_v1.constants.Constants.USER_LOGIN_STATE;

@RestController
@RequestMapping(value = "/file")
public class BigFileController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SampleDataService sampleDataService;
    @Value("${my-app.upload.uploadPath}")
    private String fileStorePath;
    private static final Logger logger = LoggerFactory.getLogger(BigFileController.class);


    @PostMapping(value = "/check")
    public BaseResponse checkBigFile(String fileMd5) {
        File mergeMd5Dir = new File(fileStorePath + "/" + "merge"+ "/" + fileMd5);
        if(mergeMd5Dir.exists()){
            return new BaseResponse<>(-1, null, "经校验MD5文件已上传，触发秒传机制");
        }

        File dir = new File(fileStorePath + "/" + fileMd5);
        File[] fileList = dir.listFiles();
        if(fileList==null){
            return new BaseResponse<>(0, null, "文件未上传过，切片1");
        }else{
            return new BaseResponse<>(fileList.length-1, null, "文件已上传过部分，切片"+fileList.length+"");
        }
    }

    @PostMapping(value = "/upload")
    public BaseResponse uploadBigFile(@ModelAttribute MultipartFileRequest multipartFileRequest) {
        String fileName = multipartFileRequest.getName();
        int chunkIndex = multipartFileRequest.getChunk();
        File file = new File(fileStorePath + "/" + multipartFileRequest.getMd5());
        if (!file.exists()) {
            file.mkdir();
        }
        File chunkFile = new File(fileStorePath + "/" +  multipartFileRequest.getMd5() + "/" + chunkIndex);
        try{
            FileUtils.copyInputStreamToFile(multipartFileRequest.getFile().getInputStream(), chunkFile);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtils.error(FILE_UPLOAD_ERROR);
        }
        return ResultUtils.success(fileName);
    }

    // TODO 测试 添加了用户登录校验并添加到数据库中后的controller DONE
    @PostMapping(value = "/merge")
    public BaseResponse uploadFileMerge(HttpServletRequest request) {
        // 获取参数
        String fileMd5 = request.getParameter("fileMd5");
        String fileName = request.getParameter("fileName");
        String fileExt = request.getParameter("fileExt");
        String sampleId = request.getParameter("sampleId");
        // 合并文件
        File file = new File(fileStorePath + "/" + fileMd5);
        File[] fileList = file.listFiles();
        File mergeFile = new File(fileStorePath + "/" + "merge"+ "/" + fileMd5);
        if(!mergeFile.exists()){
            mergeFile.mkdirs();
        }
        File mergeFileDir = new File(fileStorePath + "/" + "merge"+ "/" + fileMd5 + "/" + fileName + "." + fileExt);
        if(mergeFileDir.exists()){
            mergeFileDir.delete();
        }
        try{
            for (File f : fileList) {
                Files.copy(f.toPath(), new FileOutputStream(mergeFileDir, true));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // 将上传合并后的文件路径添加到数据库中
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            return new BaseResponse<>(-1, null, "用户未登录");
        }
        // 获取当前登录用户的ID
        // 假设您的用户实体类为SafetyUser，且包含一个getId()方法
        User currentUser = (User) userObj;
        Long userId = currentUser.getId();
        // 保存上传文件信息，保存文件路径，即文件信息与用户信息关联起来了
        // 如果md5已经存在，就不再保存
        boolean saveFlag = sampleDataService.saveUploadFile(mergeFileDir.getPath(), fileName, sampleId, userId, fileMd5);
        if (!saveFlag) {
            logger.warn("文件已存在于数据库中，不再保存");
        }
        return ResultUtils.success(mergeFileDir);
    }
}
