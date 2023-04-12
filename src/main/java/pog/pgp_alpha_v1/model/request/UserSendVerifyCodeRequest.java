package pog.pgp_alpha_v1.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSendVerifyCodeRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mail;
}
