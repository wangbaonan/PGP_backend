package pog.pgp_alpha_v1.service;

public interface CaptchaCodeService {
    boolean validateCaptchaCode(String captchaKey, String captchaCode);
}
