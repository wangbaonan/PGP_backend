package pog.pgp_alpha_v1.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 */
//@ApiModel(description = "User login request payload")
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    //@ApiModelProperty(value = "The user's account", required = true)
    private String userAccount;
    //@ApiModelProperty(value = "The user's password", required = true)
    private String userPassword;
    private String captchaKey;
    private String captchaCode;
}
