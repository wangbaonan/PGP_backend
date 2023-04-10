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
}