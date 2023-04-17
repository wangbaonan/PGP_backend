package pog.pgp_alpha_v1.model.request;

import lombok.Data;

import java.io.Serializable;

//(description = "User send verify code request payload")
@Data
public class UserSendVerifyCodeRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    //@ApiModelProperty(value = "The user's email", required = true)
    private String mail;
}
