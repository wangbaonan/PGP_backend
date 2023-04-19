package pog.pgp_alpha_v1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pog.pgp_alpha_v1.model.SampleData;

/**
* @author 86183
* @description 针对表【sample_data】的数据库操作Service
* @createDate 2023-04-19 11:03:05
*/
public interface SampleDataService extends IService<SampleData> {
    boolean saveUploadFile(String filePath, String fileName, String sampleId, Long userId, String md5Hash);
}
