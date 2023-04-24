package pog.pgp_alpha_v1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pog.pgp_alpha_v1.model.SampleData;
import pog.pgp_alpha_v1.model.User;

import java.util.ArrayList;

/**
* @author 86183
* @description 针对表【sample_data】的数据库操作Service
* @createDate 2023-04-19 11:03:05
*/
public interface SampleDataService extends IService<SampleData> {
    /**
     * 保存上传的文件
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @param sampleId 样本ID
     * @param userId 用户ID
     * @param md5Hash MD5
     * @return dataId
     */
    Long saveUploadFile(String filePath, String fileName, String sampleId, Long userId, String md5Hash);

    /**
     * 通过dataId获取样本数据
     * @param dataId 样本数据ID
     * @return 样本数据
     */
    SampleData getSampleData(Long dataId, User user);

    /**
     * 通过用户ID获取样本数据
     * @param request 请求
     * @return 样本数据列表
     */
    ArrayList<SampleData> getSampleDataByUser(User user);
}
