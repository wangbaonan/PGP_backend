package pog.pgp_alpha_v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.service.CaptchaCodeService;

@Service
public class CaptchaCodeServiceImpl implements CaptchaCodeService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean validateCaptchaCode(String captchaKey, String captchaCode) {
        String realCaptchaCode = redisTemplate.opsForValue().get(captchaKey);

        // 然后，比较用户输入的验证码和真实验证码是否相等
        if (realCaptchaCode != null && realCaptchaCode.equalsIgnoreCase(captchaCode)) {
            // 如果验证码正确，从存储中移除这个验证码，并返回true
            redisTemplate.delete(captchaKey);
            return true;
        } else {
            // 如果验证码不正确，返回false
            return false;
        }
    }
}
