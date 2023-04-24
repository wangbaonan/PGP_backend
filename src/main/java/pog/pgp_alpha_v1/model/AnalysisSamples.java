package pog.pgp_alpha_v1.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName analysis_samples
 */
@TableName(value ="analysis_samples")
@Data
public class AnalysisSamples implements Serializable {
    /**
     * 分析ID
     */
    private Long analysisId;

    /**
     * 数据ID
     */
    private Long dataId;

    /**
     * 分析结果路径
     */
    private String resultPath;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}