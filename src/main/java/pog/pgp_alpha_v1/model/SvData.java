package pog.pgp_alpha_v1.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName sv_data
 */
@TableName(value ="sv_data")
@Data
public class SvData implements Serializable {
    /**
     * 数据ID
     */
    @TableId
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
     * SV路径
     */
    private String filePath;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * MD5
     */
    private String md5Hash;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}