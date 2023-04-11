package pog.pgp_alpha_v1.service;

import pog.pgp_alpha_v1.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Wangbaonan
* @description 针对表【user】的数据库操作Service
* @createDate 2023-04-10 22:21:40*
 * 用户服务 Service主要写业务逻辑
*/
public interface UserService extends IService<User> {

    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 二次校验密码
     * @param mail 用户邮箱
     * @param username 用户名
     * @return 返回id 失败为-1
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String mail, String username);
}
