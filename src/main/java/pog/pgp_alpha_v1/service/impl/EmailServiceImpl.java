package pog.pgp_alpha_v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pog.pgp_alpha_v1.service.EmailService;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
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
        // 创建邮件正文
        Context context = new Context();
        // 设置变量
        context.setVariable("verificationCode", verificationCode);

        // 渲染模板
        String body = templateEngine.process("verification-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        // true表示需要创建一个multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        // 发送邮件
        try {
            // 发件人
            helper.setFrom("wangbaonandlut@163.com");
            helper.setTo(to);
            // 收件人
            helper.setSubject(subject);
            // 邮件内容
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
}
