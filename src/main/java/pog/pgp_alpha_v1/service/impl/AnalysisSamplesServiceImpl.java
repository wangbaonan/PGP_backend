package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.mapper.AnalysisSamplesMapper;
import pog.pgp_alpha_v1.model.AnalysisSamples;
import pog.pgp_alpha_v1.model.SampleData;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.service.AnalysisSamplesService;
import pog.pgp_alpha_v1.service.SampleDataService;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static pog.pgp_alpha_v1.utils.FileUtil.createHardLink;

/**
* @author 86183
* @description 针对表【analysis_samples】的数据库操作Service实现
* @createDate 2023-04-21 09:41:28
*/
@Service
public class AnalysisSamplesServiceImpl extends ServiceImpl<AnalysisSamplesMapper, AnalysisSamples>
    implements AnalysisSamplesService{

    @Value("${my-app.analysis.analysisPath}")
    private String analysisAllPath;
    @Resource
    private SampleDataService sampleDataService;
    // 这部分逻辑需要在新建分析的时候就创建好，这里只需要将样本添加到分析中

    /**
     * 将样本添加到分析中
     * @param analysisId 分析ID
     * @param dataIds 样本ID
     * @throws IOException
     */
    @Override
    public boolean addSamples(Long analysisId, Long[] dataIds, User user) throws IOException {
        pog.pgp_alpha_v1.model.AnalysisSamples analysisSamples = new pog.pgp_alpha_v1.model.AnalysisSamples();
        analysisSamples.setAnalysisId(analysisId);
        for(Long dataId : dataIds){
            analysisSamples.setDataId(dataId);
            Long count = this.count(new QueryWrapper<AnalysisSamples>().eq("analysisId", analysisId).eq("dataId", dataId));
            if (count != 0) {
                continue;
            }
            this.save(analysisSamples);
            // 在数据库中搜索dataId获取样本路径，并将样本硬链接到分析路径
            SampleData sample =  sampleDataService.getSampleData(dataId,user);
            String sampleSourceFilePath = sample.getFilePath();
            // 先创建分析文件夹，再将样本链接到分析文件夹中
            Path sampleTargetPath = Paths.get(analysisAllPath,analysisId.toString());
            log.warn("Source:"+sampleSourceFilePath);
            log.warn("Target"+sampleTargetPath);
            createHardLink(sampleSourceFilePath,sampleTargetPath.toString());
        }
        return true;
    }

    /**
     * 将样本从分析中移除
     */
    @Override
    public boolean removeSamples(Long analysisId, Long[] dataIds, User user) {
        for(Long dataId : dataIds){
            QueryWrapper<pog.pgp_alpha_v1.model.AnalysisSamples> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("analysisId", analysisId);
            queryWrapper.eq("dataId", dataId);
            if(this.getOne(queryWrapper) == null){
                continue;
            }
            this.remove(queryWrapper);
        }
        // 从路径中删除硬链接，但保留原始文件
        for(Long dataId : dataIds){
            SampleData sample =  sampleDataService.getSampleData(dataId, user);
            String sampleSourceFilePath = sample.getFilePath();
            Path sampleSourcePath = Paths.get(sampleSourceFilePath);
            String fileName = sampleSourcePath.getFileName().toString();
            Path sampleTargetPath = Paths.get(analysisAllPath,analysisId.toString());
            // 从数据库中获取文件名，然后删除
            Path sampleTargetFilePath = Paths.get(sampleTargetPath.toString(),fileName);
            sampleTargetFilePath.toFile().delete();
        }
        return true;
    }

    /**
     * 获取分析中的样本ID
     * @param analysisId
     * @return
     */
    @Override
    public Long[] getDataIds(Long analysisId, User currentUser) {
        QueryWrapper<AnalysisSamples> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("analysisId", analysisId);
        AnalysisSamples[] analysisSamples = this.list(queryWrapper).toArray(new AnalysisSamples[0]);
        Long[] dataIds = new Long[analysisSamples.length];
        for(int i = 0; i < analysisSamples.length; i++){
            dataIds[i] = analysisSamples[i].getDataId();
            QueryWrapper<SampleData> sampleDataQueryWrapper = new QueryWrapper<>();
            sampleDataQueryWrapper.eq("dataId", dataIds[i]);
            sampleDataQueryWrapper.eq("userId", currentUser.getId());
            if(sampleDataService.getOne(sampleDataQueryWrapper) == null){
                // 如果样本不属于当前用户，则返回空
                return null;
            }
        }
        return dataIds;
    }
}




