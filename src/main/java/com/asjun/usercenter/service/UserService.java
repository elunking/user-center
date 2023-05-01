package com.asjun.usercenter.service;


import com.asjun.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户服务接口
* @author asjun
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-05-01 14:09:10
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册逻辑接口
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 检验密码
     * @return 用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

}
