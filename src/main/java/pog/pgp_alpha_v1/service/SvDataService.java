package pog.pgp_alpha_v1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pog.pgp_alpha_v1.model.SvData;
import pog.pgp_alpha_v1.model.User;

import java.util.ArrayList;

/**
* @author 86183
* @description 针对表【sv_data】的数据库操作Service
* @createDate 2023-04-25 15:19:06
*/
public interface SvDataService extends IService<SvData> {
    Long saveUploadFile(Long dataId, String filePath, String fileName, String sampleId, Long userId, String md5Hash);
    SvData getSvData(Long dataId, User user);
    ArrayList<SvData> getSvDataList(User user);
}
