package pog.pgp_alpha_v1.service;

public interface EmailService {
    void sendVerificationEmail(String to, String subject, String verificationCode);

}
