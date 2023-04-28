package pog.pgp_alpha_v1.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName sample_data
 */
@TableName(value ="sample_data")
@Data
public class SampleData implements Serializable {
    /**
     * 数据ID
     */
    @TableId(type = IdType.AUTO)
    private Long dataId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 样本ID
     */
    private String sampleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 数据路径
     */
    private String filePath;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * MD5校验码
     */
    private String md5Hash;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}