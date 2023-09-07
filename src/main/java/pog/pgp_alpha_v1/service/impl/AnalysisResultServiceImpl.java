package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.mapper.AnalysisResultMapper;
import pog.pgp_alpha_v1.model.AnalysisResult;
import pog.pgp_alpha_v1.service.AnalysisResultService;
import pog.pgp_alpha_v1.utils.JsonFileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static pog.pgp_alpha_v1.constants.AnalysisModule.*;

/**
 * @author 86183
 * @description 针对表【analysis_result】的数据库操作Service实现
 * @createDate 2023-05-09 16:52:53
 */
@Service
public class AnalysisResultServiceImpl extends ServiceImpl<AnalysisResultMapper, AnalysisResult>
        implements AnalysisResultService {
    @Value("${my-app.analysis.analysisPath}")
    private String analysisAllPath;

    @Override
    public void updateAllResultPath(Long analysisId, String sampleId, int moduleSwitchCode) {
        AnalysisResult analysisResult = new AnalysisResult();
        analysisResult.setAnalysisId(analysisId);
        analysisResult.setSampleId(sampleId);

        if ((moduleSwitchCode & ADMIXTURE_SWITCH.getValue()) != 0) {
            Path ancestryResPath = Paths.get(analysisAllPath , String.valueOf(analysisId), "output" , sampleId , "Res_JSON" , "Admixture" , "ancestry.json");
            Path similarityResPath = Paths.get(analysisAllPath , String.valueOf(analysisId), "output" , sampleId , "Res_JSON", "Admixture" , "similarity.ED.json");
            // 在analysis_result数据库中添加相应路径
            analysisResult.setAncestry(ancestryResPath.toString());
            analysisResult.setSimilarity(similarityResPath.toString());
        } else {
            analysisResult.setAncestry(null);
            analysisResult.setSimilarity(null);
        }

        if ((moduleSwitchCode & AS2_SWITCH.getValue()) != 0) {
            Path archaicSegPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "AS2" , "archaic.seg.json");
            Path archaicSumPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "AS2" , "archaic.sum.json");
            Path davidChartReportPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "AS2" , "david.anno.chartReport.json");
            Path davidGeneClusterReportPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "AS2" , "david.anno.geneClusterReport.json");
            Path davidTermClusterReportPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "AS2" , "david.anno.termClusteringReport.json");
            Path archaicPlotPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "AS2" , "archaicChrPlot.png");
            analysisResult.setArchaicSeg(archaicSegPath.toString());
            analysisResult.setArchaicSum(archaicSumPath.toString());
            analysisResult.setDavidChartReport(davidChartReportPath.toString());
            analysisResult.setDavidGeneClusterReport(davidGeneClusterReportPath.toString());
            analysisResult.setDavidTermClusterReport(davidTermClusterReportPath.toString());
            analysisResult.setArchaicPlot(archaicPlotPath.toString());
        } else {
            analysisResult.setArchaicSeg(null);
            analysisResult.setArchaicSum(null);
            analysisResult.setDavidChartReport(null);
            analysisResult.setDavidGeneClusterReport(null);
            analysisResult.setDavidTermClusterReport(null);
            analysisResult.setArchaicPlot(null);
        }

        if ((moduleSwitchCode & HLA_SWITCH.getValue()) != 0) {
            Path hlaPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "HLA" , "HLA.json");
            analysisResult.setHla(hlaPath.toString());
        } else {
            analysisResult.setHla(null);
        }

        if ((moduleSwitchCode & MTY_SWITCH.getValue()) != 0) {
            Path mtyPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "MT_Y" , "MT_Y.json");
            analysisResult.setMty(mtyPath.toString());
        } else {
            analysisResult.setMty(null);
        }

        if ((moduleSwitchCode & PCA_SWITCH.getValue()) != 0) {
            Path pcaPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "PCA" , "PCA.json");
            analysisResult.setPca(pcaPath.toString());
        } else {
            analysisResult.setPca(null);
        }

        if ((moduleSwitchCode & PROVINCE_SWITCH.getValue()) != 0) {
            Path provincePath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "Province" , "province.json");
            analysisResult.setProvince(provincePath.toString());
        } else {
            analysisResult.setProvince(null);
        }

        if ((moduleSwitchCode & PRS_SWITCH.getValue()) != 0) {
            Path prsPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "PRS" , "prs.json");
            String prsScriptPath = "/home/pogadmin/PersonalGenome/src/scripts/PGS/process_prs.py";
            Path processedPrsPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "PRS" , "processed_prs.json");
            // Run the Python script
            ProcessBuilder processBuilder = new ProcessBuilder("/share/apps/cluster/python-3.8.13/bin/python", prsScriptPath, prsPath.toString(), processedPrsPath.toString(), sampleId);
            try {
                Process process = processBuilder.start();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            analysisResult.setPrs(processedPrsPath.toString());
        } else {
            analysisResult.setPrs(null);
        }

        if ((moduleSwitchCode & SNPEDIA_SWITCH.getValue()) != 0) {
            Path snpediaMedicalCondictionPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "Snpedia" , "snpedia.medicalCondiction.json");
            Path snpediaMedicinePath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "Snpedia" , "snpedia.medicine.json");
            analysisResult.setSnpediaMedicalCondiction(snpediaMedicalCondictionPath.toString());
            analysisResult.setSnpediaMedicine(snpediaMedicinePath.toString());
        } else {
            analysisResult.setSnpediaMedicalCondiction(null);
            analysisResult.setSnpediaMedicine(null);
        }

        if ((moduleSwitchCode & SV_SWITCH.getValue()) != 0) {
            Path svPath = Paths.get(analysisAllPath , String.valueOf(analysisId) , "output" , sampleId , "Res_JSON" , "SV" , "sv.anno.json");
            analysisResult.setSv(svPath.toString());
        } else {
            analysisResult.setSv(null);
        }

        // 向analysis_result表中插入数据
        this.save(analysisResult);
    }

    @Override
    public Object getPrsResultPath(Long analysisId, String sampleId) {
        // 获取analysis_result表中的prs字段
        String prsPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getPrs();
        Object json = JsonFileUtils.readJsonFromFile(prsPath);
        return json;
    }

    @Override
    public Object getAncestryResultPath(Long analysisId, String sampleId) {
        String ancestryPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getAncestry();
        Object json = JsonFileUtils.readJsonFromFile(ancestryPath);
        return json;
    }

    @Override
    public Object getSimilarityResultPath(Long analysisId, String sampleId) {
        String similarityPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getSimilarity();
        Object json = JsonFileUtils.readJsonFromFile(similarityPath);
        return json;
    }

    @Override
    public Object getArchaicSegResultPath(Long analysisId, String sampleId) {
        String archaicSegPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getArchaicSeg();
        Object json = JsonFileUtils.readJsonFromFile(archaicSegPath);
        return json;
    }

    @Override
    public Object getArchaicSumResultPath(Long analysisId, String sampleId) {
        String archaicSumPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getArchaicSum();
        Object json = JsonFileUtils.readJsonFromFile(archaicSumPath);
        return json;
    }

    @Override
    public Object getDavidChartReportResultPath(Long analysisId, String sampleId) {
        String davidChartReportPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getDavidChartReport();
        Object json = JsonFileUtils.readJsonFromFile(davidChartReportPath);
        return json;
    }

    @Override
    public Object getDavidGeneClusterReportResultPath(Long analysisId, String sampleId) {
        String davidGeneClusterReportPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getDavidGeneClusterReport();
        Object json = JsonFileUtils.readJsonFromFile(davidGeneClusterReportPath);
        return json;
    }

    @Override
    public Object getDavidTermClusterReportResultPath(Long analysisId, String sampleId) {
        String davidTermClusterReportPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getDavidTermClusterReport();
        Object json = JsonFileUtils.readJsonFromFile(davidTermClusterReportPath);
        return json;
    }

    // 返回的是图片路径，controller中需要将图片转为流的形式
    @Override
    public String getArchaicPlotResultPath(Long analysisId, String sampleId) {
        String archaicPlotPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getArchaicPlot();
        return archaicPlotPath;
    }

    @Override
    public Object getHlaResultPath(Long analysisId, String sampleId) {
        String hlaPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getHla();
        Object json = JsonFileUtils.readJsonFromFile(hlaPath);
        return json;
    }

    @Override
    public Object getPrsHtmlResultPath(Long analysisId, String sampleId) {
        String prsHtmlPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getPrsHtml();
        Object json = JsonFileUtils.readJsonFromFile(prsHtmlPath);
        return json;
    }

    @Override
    public Object getMtyResultPath(Long analysisId, String sampleId) {
        String mtyPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getMty();
        Object json = JsonFileUtils.readJsonFromFile(mtyPath);
        return json;
    }

    @Override
    public Object getPcaResultPath(Long analysisId, String sampleId) {
        String pcaPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getPca();
        Object json = JsonFileUtils.readJsonFromFile(pcaPath);
        return json;
    }

    @Override
    public Object getProvinceResultPath(Long analysisId, String sampleId) {
        String provincePath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getProvince();
        Object json = JsonFileUtils.readJsonFromFile(provincePath);
        return json;
    }

    @Override
    public Object getSnpediaMedicalCondictionResultPath(Long analysisId, String sampleId) {
        String snpediaMedicalCondictionPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getSnpediaMedicalCondiction();
        Object json = JsonFileUtils.readJsonFromFile(snpediaMedicalCondictionPath);
        return json;
    }

    @Override
    public Object getSnpediaMedicineResultPath(Long analysisId, String sampleId) {
        String snpediaMedicinePath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getSnpediaMedicine();
        Object json = JsonFileUtils.readJsonFromFile(snpediaMedicinePath);
        return json;
    }

    @Override
    public Object getSvResultPath(Long analysisId, String sampleId) {
        String svPath = this.getOne(new QueryWrapper<AnalysisResult>().eq("analysisId", analysisId).eq("sampleId", sampleId)).getSv();
        Object json = JsonFileUtils.readJsonFromFile(svPath);
        return json;
    }
}




