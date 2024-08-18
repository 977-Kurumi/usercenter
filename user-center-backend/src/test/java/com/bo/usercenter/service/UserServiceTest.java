package com.bo.usercenter.service;

import com.bo.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * 用户服务测试
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("977@");
        user.setUserAccount("977");
        user.setAvatarUrl("https://baomidou.com/assets/asset.cIbiVTt_.svg");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("123");
        user.setEmail("456");
        boolean save = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(save);
    }

    @Test
    void getBaseMapper() {
    }

    @Test
    void userRegister() {
        String username = "haha";
        String password = "";
        String checkPassword = "123456";
        long l = userService.userRegister(username, password, checkPassword);
        Assertions.assertEquals(-1,l);
        username = "ha";
        l = userService.userRegister(username, password, checkPassword);
        Assertions.assertEquals(-1,l);
        username = "haha";
        password = "12345678";
        l = userService.userRegister(username, password, checkPassword);
        Assertions.assertEquals(-1,l);
        username = "ha ha";
        checkPassword = "12345678";
        l = userService.userRegister(username, password, checkPassword);
        Assertions.assertEquals(-1,l);
        checkPassword = "123456789";
        l = userService.userRegister(username, password, checkPassword);
        Assertions.assertEquals(-1,l);
        username = "977";
        checkPassword = "12345678";
        l = userService.userRegister(username, password, checkPassword);
        Assertions.assertEquals(-1,l);
        username = "977a";
        l = userService.userRegister(username, password, checkPassword);
//        Assertions.assertTrue(l>0);
        Assertions.assertEquals(-1,l);

    }
}