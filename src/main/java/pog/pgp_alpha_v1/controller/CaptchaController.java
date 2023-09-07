package pog.pgp_alpha_v1.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pog.pgp_alpha_v1.common.BaseResponse;
import pog.pgp_alpha_v1.common.ResultUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(value = "/captcha", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<Map<String, Object>> getCaptcha() {
        // 生成验证码文本
        String captchaText = captchaProducer.createText();

        // 生成UUID作为键
        String captchaKey = UUID.randomUUID().toString();

        // 把验证码文本存入Redis，设置过期时间为5分钟
        redisTemplate.opsForValue().set(captchaKey, captchaText, 5, TimeUnit.MINUTES);

        // 创建验证码图片
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);

        // 将图片转换为byte数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(captchaImage, "jpg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将byte数组转换为Base64编码的字符串
        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());

        // 返回验证码图片和UUID
        Map<String, Object> captchaMap = new HashMap<>();
        captchaMap.put("captchaKey", captchaKey);
        captchaMap.put("captchaImage", base64Image);

        return ResultUtils.success(captchaMap);
    }
}



