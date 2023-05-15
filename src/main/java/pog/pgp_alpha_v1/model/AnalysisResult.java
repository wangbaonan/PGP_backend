package pog.pgp_alpha_v1.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName analysis_result
 */
@TableName(value ="analysis_result")
@Data
public class AnalysisResult implements Serializable {
    /**
     * 分析ID
     */
    @TableId
    private Long analysisId;

    /**
     * 样本ID
     */
    private String sampleId;

    /**
     * PRS结果路径
     */
    private String prs;

    /**
     * 祖源结果路径
     */
    private String ancestry;

    /**
     * 祖源相似度
     */
    private String similarity;

    /**
     * 古人渗入片段
     */
    private String archaicSeg;

    /**
     * 古人渗入总结
     */
    private String archaicSum;

    /**
     * 
     */
    private String davidChartReport;

    /**
     * 
     */
    private String davidGeneClusterReport;

    /**
     * 
     */
    private String davidTermClusterReport;

    /**
     * 
     */
    private String archaicPlot;

    /**
     * PCA结果路径路径
     */
    private String pca;

    /**
     * 结构变异路径
     */
    private String sv;

    /**
     * HLA分型结果路径
     */
    private String hla;

    /**
     * 
     */
    private String prsHtml;

    /**
     * 省份结果路径
     */
    private String province;

    /**
     * MT和Y单倍群结果路径
     */
    private String mty;

    /**
     * 古人渗入结果路径
     */
    private String as2;

    /**
     * SNPedia数据库注释结果路径
     */
    private String snpediaMedicine;

    /**
     * 
     */
    private String snpediaMedicalCondiction;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}