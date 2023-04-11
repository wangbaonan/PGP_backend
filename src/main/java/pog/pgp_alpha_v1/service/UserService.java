package pog.pgp_alpha_v1.service;

import pog.pgp_alpha_v1.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wangbaonan
 * @description 针对表【user】的数据库操作Service
 * @createDate 2023-04-10 22:21:40*
 * 用户服务 Service主要写业务逻辑
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 二次校验密码
     * @param mail          用户邮箱
     * @param username      用户名
     * @return 返回id 失败为-1
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String mail, String username);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 返回脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);
}
