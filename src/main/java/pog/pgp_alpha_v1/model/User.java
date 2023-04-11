package pog.pgp_alpha_v1.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 帐号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 用户名
     */
    private String username;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 角色
     */
    private Integer userRole;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}