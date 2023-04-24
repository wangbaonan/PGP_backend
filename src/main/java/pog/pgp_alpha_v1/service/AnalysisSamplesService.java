package pog.pgp_alpha_v1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pog.pgp_alpha_v1.model.AnalysisSamples;
import pog.pgp_alpha_v1.model.User;

import java.io.IOException;

/**
* @author 86183
* @description 针对表【analysis_samples】的数据库操作Service
* @createDate 2023-04-21 09:41:28
*/
public interface AnalysisSamplesService extends IService<AnalysisSamples> {
    // 向指定分析中添加样本
    boolean addSamples(Long analysisId, Long[] dataIds, User user) throws IOException;

    // 根据分析ID获取样本ID
    Long[] getDataIds(Long analysisId, User currentUser);

    // 将样本从分析中移除
    boolean removeSamples(Long analysisId, Long[] dataIds, User user);
}
