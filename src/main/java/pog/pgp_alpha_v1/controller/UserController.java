package pog.pgp_alpha_v1.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.model.request.UserLoginRequest;
import pog.pgp_alpha_v1.model.request.UserRegisterRequest;
import pog.pgp_alpha_v1.model.request.UserSendVerifyCodeRequest;
import pog.pgp_alpha_v1.model.request.UserVerifyRequest;
import pog.pgp_alpha_v1.service.EmailService;
import pog.pgp_alpha_v1.service.UserService;
import pog.pgp_alpha_v1.service.VerificationCodeService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static pog.pgp_alpha_v1.constant.Constants.ADMIN_ROLE;
import static pog.pgp_alpha_v1.constant.Constants.USER_LOGIN_STATE;

/**
 * 用户接口
 * @author Wangbaonan
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private VerificationCodeService verificationCodeService;
    @Resource
    private EmailService emailService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            return null;
        }
        // 从前端传来的请求体中获取参数
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String username = userRegisterRequest.getUsername();
        String mail = userRegisterRequest.getMail();
        // 本处校验不对业务逻辑进行校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, username, mail)){
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            return null;
        }
        // 从前端传来的请求体中获取参数
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 本处校验不对业务逻辑进行校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request){
        if(isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username); //模糊查询
        }
        return userService.list();
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request){
        if(!isAdmin(request)){
            return false;
        }
        if(id <= 0){
            return false;
        }
        // 逻辑删除
        return userService.removeById(id);
    }

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody UserSendVerifyCodeRequest userSendVerifyCodeRequest) {
        // 获取邮箱
        String email = userSendVerifyCodeRequest.getMail();
        // 生成验证码
        String verificationCode = verificationCodeService.generateVerificationCode();
        // 将邮箱与验证码绑定 存入Redis 并发送邮件
        verificationCodeService.storeVerificationCode(email, verificationCode);
        emailService.sendVerificationEmail(email, "Verification Code", verificationCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public String verify(@RequestBody UserVerifyRequest userVerifyRequest, RedirectAttributes redirectAttributes){
        String email = userVerifyRequest.getMail();
        String submittedCode = userVerifyRequest.getVerificationCode();
        if (verificationCodeService.verifyCode(email, submittedCode)) {
            // 验证成功 更新用户状态 删除验证码 重定向到登录页面 并提示成功
            userService.markUserAsVerified(email);
            verificationCodeService.deleteVerificationCode(email);
            /**
             * 这段代码是在用户进行账号验证后，验证成功后跳转到登录页面的功能实现代码。
             * 在验证成功后，会将一个名为"message"，值为"Verification successful"的属性加入到Flash Attribute中，
             * 然后通过重定向方式跳转到登录页面。Flash Attribute是一种特殊的attribute，用于在两个请求之间传递数据。
             * 这种方式可以在重定向时把数据放入到session中，然后在重定向后的页面中立即取出并使用，而不是在URL中暴露数据。
             *
             * 前端接收到"redirect:/login"时，会向服务器发出新的请求，请求/login接口，然后跳转到登录页面。而在登录页面中，
             * 可以通过EL表达式取出之前加入到Flash Attribute中的"message"属性值，以供页面展示使用。
             */
            redirectAttributes.addFlashAttribute("message", "Verification successful");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid verification code");
            return "redirect:/verification";
        }
    }

    /**
     * 鉴权 是否为管理员
     * @param request HttpServletRequest
     * @return T-管理员 F-非管理员
     */
    private boolean isAdmin(HttpServletRequest request){
        // 鉴权
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole().equals(ADMIN_ROLE);
    }
}
