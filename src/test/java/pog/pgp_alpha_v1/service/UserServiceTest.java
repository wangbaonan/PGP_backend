package pog.pgp_alpha_v1.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    /**
     * 数据库连接测试
     */
    @Test
    public void testAddUser(){
    }

    /**
     * 注册测试方法
     */
    @Test
    void userRegister() {
    }
}