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
    Object getPrsResultPath(Long analysisId, String sampleId);
    Object getAncestryResultPath(Long analysisId, String sampleId);
    Object getSimilarityResultPath(Long analysisId, String sampleId);
    Object getArchaicSegResultPath(Long analysisId, String sampleId);
    Object getArchaicSumResultPath(Long analysisId, String sampleId);
    Object getDavidChartReportResultPath(Long analysisId, String sampleId);
    Object getDavidGeneClusterReportResultPath(Long analysisId, String sampleId);
    Object getDavidTermClusterReportResultPath(Long analysisId, String sampleId);
    String getArchaicPlotResultPath(Long analysisId, String sampleId);
    Object getPcaResultPath(Long analysisId, String sampleId);
    Object getSvResultPath(Long analysisId, String sampleId);
    Object getHlaResultPath(Long analysisId, String sampleId);
    Object getPrsHtmlResultPath(Long analysisId, String sampleId);
    Object getProvinceResultPath(Long analysisId, String sampleId);
    Object getMtyResultPath(Long analysisId, String sampleId);
    Object getSnpediaMedicineResultPath(Long analysisId, String sampleId);
    Object getSnpediaMedicalCondictionResultPath(Long analysisId, String sampleId);
}
