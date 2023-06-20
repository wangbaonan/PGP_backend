package pog.pgp_alpha_v1.utils;

import org.apache.commons.io.FileUtils;
import pog.pgp_alpha_v1.common.BaseResponse;
import pog.pgp_alpha_v1.common.ResultUtils;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.model.request.MultipartFileRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.Map;

import static pog.pgp_alpha_v1.common.ErrorCode.FILE_UPLOAD_ERROR;
import static pog.pgp_alpha_v1.constants.Constants.USER_LOGIN_STATE;

public class UploadUtils {
    public static BaseResponse checkMd5(String fileMd5, String fileStorePath) {
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

    public static BaseResponse uploadFile(MultipartFileRequest multipartFileRequest, String fileStorePath) {
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

    public static File mergeFile(String fileMd5, String fileName,String fileExt, String fileStorePath) {
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
        return mergeFileDir;
    }

    public static Long getCurrentUserId(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null){
            return null;
        }
        return user.getId();
    }

    public static String getSampleId(String filePath){
        String sampleId = null;

        try {
            // Run bcftools query -l command
            ProcessBuilder pb = new ProcessBuilder("bcftools", "query", "-l", filePath);
            Process p = pb.start();
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // Read sample ID from bcftools output
            sampleId = reader.readLine();

        } catch (IOException e) {
            // Handle the situation when the IO operation fails, maybe log the exception and return or throw a runtime exception.
            e.printStackTrace();
        } catch (InterruptedException e) {
            // Handle the situation when the thread gets interrupted, maybe log the exception and return or throw a runtime exception.
            e.printStackTrace();
        }

        // Ensure that sampleId was successfully retrieved
        if (sampleId == null) {
            // Handle this situation, maybe log an error and return or throw a runtime exception.
            return null;
        }
        return sampleId;
    }

    public static Map.Entry<Long, File> mergeFileMap(HttpServletRequest request, String fileStorePath){
        String fileMd5 = request.getParameter("fileMd5");
        String fileName = request.getParameter("fileName");
        String fileExt = request.getParameter("fileExt");
        File mergeFileDir = mergeFile(fileMd5, fileName, fileExt, fileStorePath);
        // 将上传合并后的文件路径添加到数据库中
        Long userId =  getCurrentUserId(request);
        return new AbstractMap.SimpleEntry<>(userId, mergeFileDir);
    }
}
