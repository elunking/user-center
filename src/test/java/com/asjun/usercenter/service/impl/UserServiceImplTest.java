package com.asjun.usercenter.service.impl;


import com.asjun.usercenter.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    UserService userService;


    @Test
    void userRegister() {
        String userAccount = "startpick";
        String userPassword = "123456789";
        String checkPassword = "123456789";
        String planetId = "123";
        //判断非空
//        userAccount ="";
//        long l = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, l);

        //账户不小于4位
//        userAccount = "uui";
//        long l = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, l);

//        密码不小于8位
//        userPassword = "asfa3";
//        long l = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, l);

//        账户是否包含特殊字符
//        userAccount = "123$;3g";
//        long l = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, l);

//        密码与校验密码是否相同
//        checkPassword = "123124553";
//        long l = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, l);

//        账户是否重复
//        userAccount ="pityjun";
//        long l = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, l);

        planetId = "123";
        long l = userService.userRegister(userAccount, userPassword, checkPassword,planetId);
        Assertions.assertEquals(-1,l);

        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetId);
        System.out.println(result);
    }
}