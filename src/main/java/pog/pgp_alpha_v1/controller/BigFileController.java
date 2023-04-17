package pog.pgp_alpha_v1.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import pog.pgp_alpha_v1.common.BaseResponse;
import pog.pgp_alpha_v1.common.ResultUtils;
import pog.pgp_alpha_v1.model.request.MultipartFileRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

@RestController
@RequestMapping(value = "/file")
public class BigFileController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${my-app.upload.uploadPath}")
    private String fileStorePath;

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
        }
        return ResultUtils.success(fileName);
    }

    @PostMapping(value = "/merge")
    public BaseResponse uploadFileMerge(HttpServletRequest request) {
        String fileMd5 = request.getParameter("fileMd5");
        String fileName = request.getParameter("fileName");
        String fileExt = request.getParameter("fileExt");
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
        return ResultUtils.success(mergeFileDir);
    }
}
