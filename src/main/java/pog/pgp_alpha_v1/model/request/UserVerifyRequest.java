package pog.pgp_alpha_v1.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户验证请求体
 */
//@ApiModel(description = "User verify request payload")
@Data
public class UserVerifyRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    //@ApiModelProperty(value = "The user's email", required = true)
    private String mail;
    //@ApiModelProperty(value = "The user's verification code", required = true)
    private String verificationCode;
}
