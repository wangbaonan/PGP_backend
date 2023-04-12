package pog.pgp_alpha_v1.service.impl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.service.VerificationCodeService;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static pog.pgp_alpha_v1.constant.Constants.*;

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
        // 10分钟过期
        stringRedisTemplate.opsForValue().set(email, verificationCode, VERIFY_CODE_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    /**
     * 验证验证码
     * @param email 邮箱
     * @param submittedCode 提交的验证码
     * @return 是否正确
     */
    @Override
    public boolean verifyCode(String email, String submittedCode) {
        // 获取存储的验证码
        String storedCode = stringRedisTemplate.opsForValue().get(email);
        return storedCode != null && storedCode.equals(submittedCode);
    }

    public String generateVerificationCode() {
        int code = (int) (Math.random() * (VERIFY_CODE_MAX - VERIFY_CODE_MIN) + VERIFY_CODE_MIN);
        return String.valueOf(code);
    }

    /**
     * 删除验证码
     * @param email 邮箱
     */
    @Override
    public void deleteVerificationCode(String email) {
        // 删除验证码
        stringRedisTemplate.delete(email);
    }
}
