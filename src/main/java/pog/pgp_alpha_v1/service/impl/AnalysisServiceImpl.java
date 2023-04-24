package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.mapper.AnalysisMapper;
import pog.pgp_alpha_v1.model.Analysis;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.service.AnalysisService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static pog.pgp_alpha_v1.utils.FileUtil.createDirectoriesRecursively;

/**
* @author 86183
* @description 针对表【analysis】的数据库操作Service实现
* @createDate 2023-04-19 15:18:15
*/
@Service
public class AnalysisServiceImpl extends ServiceImpl<AnalysisMapper, Analysis>
    implements AnalysisService{

    @Value("${my-app.analysis.analysisPath}")
    private String analysisAllPath;

    @Override
    public Long createAnalysis(User user) {
        if (user != null){
            Analysis analysis = new Analysis();
            analysis.setUserId((user).getId());
            this.save(analysis);
            // 创建User对应的文件夹以及分析ID对应的文件夹
            Long analysisId = analysis.getAnalysisId();
            // Linux环境下路径可能需要换符号 使用Path来实现跨系统的路径拼接
            Path analysisPath = Paths.get(analysisAllPath, analysisId.toString());
            // 对应分析ID的路径已经构建完成
            createDirectoriesRecursively(analysisPath.toString());
            return analysis.getAnalysisId();
        }
        return null;
    }

    @Override
    public ArrayList<Analysis> getAnalysisList(User user) {
        if (user != null){
            // 使用QueryWrapper来实现基于用户ID查询分析列表的方法
            QueryWrapper<Analysis> queryWrapper = new QueryWrapper<>();
            Long userId = user.getId();
            queryWrapper.eq("userId", userId);
            ArrayList<Analysis> analysisList = (ArrayList<Analysis>) this.list(queryWrapper);
            return analysisList;
        }
        return null;
    }

    @Override
    public boolean deleteAnalysis(Long analysisId, Long userId) {
        if (analysisId != null && userId != null){
            QueryWrapper<Analysis> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("analysisId", analysisId);
            queryWrapper.eq("userId", userId);
            return this.remove(queryWrapper);
        }
        return false;
    }
}




