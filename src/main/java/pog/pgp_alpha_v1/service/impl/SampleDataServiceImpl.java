package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pog.pgp_alpha_v1.model.SampleData;
import pog.pgp_alpha_v1.service.SampleDataService;
import pog.pgp_alpha_v1.mapper.SampleDataMapper;
import org.springframework.stereotype.Service;

/**
* @author 86183
* @description 针对表【sample_data】的数据库操作Service实现
* @createDate 2023-04-19 11:03:05
*/
@Service
public class SampleDataServiceImpl extends ServiceImpl<SampleDataMapper, SampleData>
    implements SampleDataService{
    @Override
    public boolean saveUploadFile(String filePath, String fileName, String sampleId, Long userId, String md5Hash) {
        pog.pgp_alpha_v1.model.SampleData sampleData = new pog.pgp_alpha_v1.model.SampleData();
        sampleData.setFilePath(filePath);
        sampleData.setFileName(fileName);
        sampleData.setSampleId(sampleId);
        sampleData.setUserId(userId);
        sampleData.setMd5Hash(md5Hash);
        // 如果md5已经存在，就不再保存
        if (this.getOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<pog.pgp_alpha_v1.model.SampleData>().eq("md5Hash", md5Hash)) == null){
            this.save(sampleData);
            return true;
        }
        return false;
    }
}




