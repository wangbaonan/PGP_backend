package pog.pgp_alpha_v1.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pog.pgp_alpha_v1.model.User;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    /**
     * 数据库连接测试
     */
    @Test
    public void testAddUser(){
        User user = new User();
        user.setUserAccount("1264295344");
        user.setUserPassword("1106801000");
        user.setMail("wangbaonandlut@163.com");
        user.setUserStatus(0);
        user.setUsername("srEvilMonk");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);

        boolean result =  userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    /**
     * 注册测试方法
     */
    @Test
    void userRegister() {
        String userAccount = "testAccount";
        String userPassword = "";
        String checkPassword = "ss";
        String mail = "wangbaonandlut@163.com";
        String username = "SrEvilMonk";
        long userRegister = userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
        Assertions.assertEquals(-1, userRegister);

        // 账户小于4位
        userAccount = "sd";
        userRegister = userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
        Assertions.assertEquals(-1, userRegister);

        // 密码小于8位
        userAccount = "testAccount";
        userPassword = "123456";
        userRegister = userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
        Assertions.assertEquals(-1, userRegister);

        // 特殊字符
        userAccount = "test Account";
        userPassword = "12345678";
        userRegister = userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
        Assertions.assertEquals(-1, userRegister);

        // 二次校验密码
        checkPassword = "123456789";
        userRegister = userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
        Assertions.assertEquals(-1, userRegister);

        // 重复用户
        userAccount = "1264295344";
        checkPassword = "12345678";
        userRegister = userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
        Assertions.assertEquals(-1, userRegister);

        // 邮箱格式
        mail = "sssX@";
        userAccount = "wangbaonan4";
        checkPassword = "12345678";
        userRegister = userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
        Assertions.assertEquals(-1, userRegister);

        // 成功注册
        userAccount = "wangbaonan4";
        mail = "sssx@163.com";
        userRegister = userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
        Assertions.assertTrue(userRegister > 0); // id >0
    }
}