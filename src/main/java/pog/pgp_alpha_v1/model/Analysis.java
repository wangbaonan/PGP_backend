package pog.pgp_alpha_v1.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName analysis
 */
@TableName(value ="analysis")
@Data
public class Analysis implements Serializable {
    /**
     * 分析ID
     */
    @TableId(type = IdType.AUTO)
    private Long analysisId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 完成时间
     */
    private Date updateTime;

    /**
     * 状态
     */
    private Integer analysisStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}