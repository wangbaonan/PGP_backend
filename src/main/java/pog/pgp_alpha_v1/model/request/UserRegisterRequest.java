package pog.pgp_alpha_v1.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
//@ApiModel(description = "User registration request payload")
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    //@ApiModelProperty(value = "The user's account", required = true)
    private String userAccount;
    //@ApiModelProperty(value = "The user's password", required = true)
    private String userPassword;
    //@ApiModelProperty(value = "The user's checkPassword", required = true)
    private String checkPassword;
    //@ApiModelProperty(value = "The user's email", required = true)
    private String mail;
    //@ApiModelProperty(value = "The user's username", required = true)
    private String username;
}
