package pog.pgp_alpha_v1.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户验证请求体
 */
@Data
public class UserVerifyRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mail;
    private String verificationCode;
}
