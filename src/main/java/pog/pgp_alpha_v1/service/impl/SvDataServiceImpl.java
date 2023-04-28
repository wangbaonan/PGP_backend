package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.mapper.SvDataMapper;
import pog.pgp_alpha_v1.model.SvData;
import pog.pgp_alpha_v1.service.SvDataService;

/**
* @author 86183
* @description 针对表【sv_data】的数据库操作Service实现
* @createDate 2023-04-25 15:19:06
*/
@Service
public class SvDataServiceImpl extends ServiceImpl<SvDataMapper, SvData>
    implements SvDataService{

    @Override
    public Long saveUploadFile(Long dataId, String filePath, String fileName, String sampleId, Long userId, String md5Hash) {
        SvData svData = new SvData();
        svData.setDataId(dataId);
        svData.setFilePath(filePath);
        svData.setSampleId(sampleId);
        svData.setUserId(userId);
        svData.setFileName(fileName);
        svData.setMd5Hash(md5Hash);
        // 如果md5已经存在，就不再保存
        if (this.getOne(new QueryWrapper<SvData>().eq("md5Hash", md5Hash)) == null){
            this.save(svData);
            return svData.getDataId();
        }
        return null;
    }
}




