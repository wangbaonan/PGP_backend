package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.mapper.SampleDataMapper;
import pog.pgp_alpha_v1.model.SampleData;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.service.SampleDataService;

import java.util.ArrayList;

/**
 * @author 86183
 * @description 针对表【sample_data】的数据库操作Service实现
 * @createDate 2023-04-19 11:03:05
 */
@Service
public class SampleDataServiceImpl extends ServiceImpl<SampleDataMapper, SampleData>
        implements SampleDataService {

    @Override
    public Long saveUploadFile(String filePath, String fileName, String sampleId, Long userId, String md5Hash) {
        SampleData sampleData = new SampleData();
        sampleData.setFilePath(filePath);
        sampleData.setFileName(fileName);
        sampleData.setSampleId(sampleId);
        sampleData.setUserId(userId);
        sampleData.setMd5Hash(md5Hash);
        // 如果md5已经存在，就不再保存
        if (this.getOne(new QueryWrapper<SampleData>().eq("md5Hash", md5Hash)) == null){
            this.save(sampleData);
            return sampleData.getDataId();
        }
        return null;
    }

    @Override
    public SampleData getSampleData(Long dataId, User user) {
        QueryWrapper<SampleData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dataId",dataId);
        queryWrapper.eq("userId",user.getId());
        return this.getOne(queryWrapper);
    }

    @Override
    public ArrayList<SampleData> getSampleDataByUser(User user) {
        // 从session中获取用户信息
        Long userId = user.getId();
        QueryWrapper<SampleData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        ArrayList<SampleData> sampleDataArrayList = (ArrayList<SampleData>) this.list(queryWrapper);
        return sampleDataArrayList;
    }
}




