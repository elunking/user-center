package com.asjun.usercenter.service.impl;

import com.asjun.usercenter.mapper.UserMapper;
import com.asjun.usercenter.model.domain.User;
import com.asjun.usercenter.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author asjun
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-05-01 14:09:10
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}




