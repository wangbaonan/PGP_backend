package pog.pgp_alpha_v1.service.impl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.service.VerificationCodeService;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 存储验证码
     * @param email 邮箱
     * @param verificationCode 验证码
     */
    @Override
    public void storeVerificationCode(String email, String verificationCode) {
        stringRedisTemplate.opsForValue().set(email, verificationCode, 10, TimeUnit.MINUTES);
    }

    /**
     * 验证验证码
     * @param email 邮箱
     * @param submittedCode 提交的验证码
     * @return
     */
    @Override
    public boolean verifyCode(String email, String submittedCode) {
        String storedCode = stringRedisTemplate.opsForValue().get(email);
        return storedCode != null && storedCode.equals(submittedCode);
    }

    /**
     * 删除验证码
     * @param email 邮箱
     */
    @Override
    public void deleteVerificationCode(String email) {
        stringRedisTemplate.delete(email);
    }
}
