package pog.pgp_alpha_v1.service.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pog.pgp_alpha_v1.service.EmailService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private TemplateEngine templateEngine;


    /**
     * 发送验证码邮件
     * @param to 邮箱
     * @param subject 主题
     * @param verificationCode 验证码
     */
    @Override
    public void sendVerificationEmail(String to, String subject, String verificationCode) {
        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);

        String body = templateEngine.process("verification-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
}
