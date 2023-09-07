package pog.pgp_alpha_v1.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pog.pgp_alpha_v1.common.BaseResponse;
import pog.pgp_alpha_v1.common.ErrorCode;
import pog.pgp_alpha_v1.common.ResultUtils;
import pog.pgp_alpha_v1.exception.BusinessException;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.model.request.*;
import pog.pgp_alpha_v1.service.CaptchaCodeService;
import pog.pgp_alpha_v1.service.EmailService;
import pog.pgp_alpha_v1.service.UserService;
import pog.pgp_alpha_v1.service.VerificationCodeService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static pog.pgp_alpha_v1.constants.Constants.*;

/**
 * 用户接口
 * @author Wangbaonan
 */
//@Api(tags = "User Management")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private VerificationCodeService verificationCodeService;
    @Resource
    private CaptchaCodeService captchaCodeService;
    @Resource
    private EmailService emailService;

    //@ApiOperation(value = "Register", response = BaseResponse.class)

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册请求体
     * @return 用户信息
     */
    @PostMapping("/register")
    public BaseResponse userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 从前端传来的请求体中获取参数
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String username = userRegisterRequest.getUsername();
        String mail = userRegisterRequest.getMail();
        String captchaKey = userRegisterRequest.getCaptchaKey();
        String captchaCode = userRegisterRequest.getCaptchaCode();
        // 本处校验不对业务逻辑进行校验
        // 邮箱可以为空，但是不为空时需要校验格式
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, username)){
            return null;
        }
        // 验证captchaCode
        if(!captchaCodeService.validateCaptchaCode(captchaKey, captchaCode)){
            return ResultUtils.error(ErrorCode.CAPTCHA_CODE_ERROR);
        }
        Object result = userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
        if(result instanceof Long){
            return ResultUtils.success(result);
        }else {
            return ResultUtils.error((ErrorCode) result);
        }
    }

    // TODO 取消了BaseResponse的<>泛型，未验证是否会出现问题
    //@ApiOperation(value = "Login", response = BaseResponse.class)

    /**
     * 用户登录
     * @param userLoginRequest 用户登录请求体
     * @param request 请求
     * @return 用户信息
     */
    @PostMapping("/login")
    public BaseResponse userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 从前端传来的请求体中获取参数
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 本处校验不对业务逻辑进行校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        String captchaKey = userLoginRequest.getCaptchaKey();
        String captchaCode = userLoginRequest.getCaptchaCode();
        // 验证captchaCode
        if(!captchaCodeService.validateCaptchaCode(captchaKey, captchaCode)){
            return ResultUtils.error(ErrorCode.CAPTCHA_CODE_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);

        if(user == null){
            // 用户名或密码错误
            return ResultUtils.error(ErrorCode.USER_LOGIN_ERROR);
        }
        else if (user.getUserVerify().equals(0)) {
            // 用户未激活 需要判断是否包含邮箱 如果该用户注册时未提供邮箱就直接登录成功
            if(!StringUtils.isBlank(user.getMail())){
                return ResultUtils.error(ErrorCode.USER_NOT_ACTIVATED);
            }
        }
        return ResultUtils.success(user);
    }
    //@ApiOperation(value = "Search", response = BaseResponse.class)

    /**
     * 搜索用户
     * @param username 用户名
     * @param request 请求
     * @return 用户列表
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){
        if(isAdmin(request)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username); //模糊查询
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    //@ApiOperation(value = "Delete", response = BaseResponse.class)

    /**
     * 删除用户
     * @param userDeleteRequest 请求体
     * @param request 请求
     * @return 成功返回true
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request){
        Long userId = userDeleteRequest.getUserId();
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if(userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 逻辑删除
        boolean deleteFlag = userService.removeById(userId);
        return ResultUtils.success(deleteFlag);
    }

    //@ApiOperation(value = "Send verification code", response = BaseResponse.class)

    /**
     * 发送验证码
     * @param userSendVerifyCodeRequest Request
     * @return 成功返回成功信息
     */
    @PostMapping("/sendVerificationCode")
    public BaseResponse<String> sendVerificationCode(@RequestBody UserSendVerifyCodeRequest userSendVerifyCodeRequest) {
        // 获取邮箱
        String email = userSendVerifyCodeRequest.getMail();
        // 判断邮箱是否已经注册如果已经注册但是未激活则直接发送验证码 如果已经注册并且已经激活则返回错误信息
        if (userService.isEmailVerified(email)) {
            return ResultUtils.error(ErrorCode.EMAIL_REGISTERED);
        }
        // 生成验证码
        String verificationCode = verificationCodeService.generateVerificationCode();
        // 将邮箱与验证码绑定 存入Redis 并发送邮件
        verificationCodeService.storeVerificationCode(email, verificationCode);
        emailService.sendVerificationEmail(email, "Verification Code", verificationCode);
        return ResultUtils.success(VERIFY_CODE_EMAIL_SEND_SUCCESS);
    }

    /**
     * 验证用户邮箱
     * @param userVerifyRequest 用户邮箱和验证码
     * @param redirectAttributes 重定向属性
     * @return 成功后重定向到登录页面 失败则返回错误信息且跳转至验证页面
     */
    //@ApiOperation(value = "Verify user's code", response = BaseResponse.class)
    @PostMapping("/verify")
    public String verify(@RequestBody UserVerifyRequest userVerifyRequest, RedirectAttributes redirectAttributes){
        String email = userVerifyRequest.getMail();
        String submittedCode = userVerifyRequest.getVerificationCode();
        // 如果邮箱已经被验证过了
        if (userService.isEmailVerified(email)) {
            return ResultUtils.error(ErrorCode.EMAIL_REGISTERED).toString();
        }
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
     * 获取当前用户信息
     * @param request HttpServletRequest
     * @return User
     */
    //@ApiOperation(value = "Get current user", response = BaseResponse.class)
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 用户登出
     * @param request HttpServletRequest
     * @return int
     */
    //@ApiOperation(value = "Logout", response = BaseResponse.class)
    @PostMapping("/logout")
    public BaseResponse<String> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(LOGOUT_SUCCESS);
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
