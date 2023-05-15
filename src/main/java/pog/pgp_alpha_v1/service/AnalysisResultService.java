package pog.pgp_alpha_v1.service;

import pog.pgp_alpha_v1.model.AnalysisResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86183
* @description 针对表【analysis_result】的数据库操作Service
* @createDate 2023-05-09 16:52:53
*/
public interface AnalysisResultService extends IService<AnalysisResult> {
    //根据提供的分析ID和样本ID，更新对应的分析结果路径
    void updateAllResultPath(Long analysisId, String sampleId, int moduleSwitchCode);

    //获取分析结果的路径
    String getPrsResultPath(Long analysisId, String sampleId);
    String getAncestryResultPath(Long analysisId, String sampleId);
    String getSimilarityResultPath(Long analysisId, String sampleId);
    String getArchaicSegResultPath(Long analysisId, String sampleId);
    String getArchaicSumResultPath(Long analysisId, String sampleId);
    String getDavidChartReportResultPath(Long analysisId, String sampleId);
    String getDavidGeneClusterReportResultPath(Long analysisId, String sampleId);
    String getDavidTermClusterReportResultPath(Long analysisId, String sampleId);
    String getArchaicPlotResultPath(Long analysisId, String sampleId);
    String getPcaResultPath(Long analysisId, String sampleId);
    String getSvResultPath(Long analysisId, String sampleId);
    String getHlaResultPath(Long analysisId, String sampleId);
    String getPrsHtmlResultPath(Long analysisId, String sampleId);
    String getProvinceResultPath(Long analysisId, String sampleId);
    String getMtyResultPath(Long analysisId, String sampleId);
    String getSnpediaMedicineResultPath(Long analysisId, String sampleId);
    String getSnpediaMedicalCondictionResultPath(Long analysisId, String sampleId);
}
