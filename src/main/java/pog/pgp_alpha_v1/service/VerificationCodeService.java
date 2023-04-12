package pog.pgp_alpha_v1.service;

public interface VerificationCodeService {
    void storeVerificationCode(String email, String verificationCode);
    boolean verifyCode(String email, String submittedCode);
    void deleteVerificationCode(String email);
}
