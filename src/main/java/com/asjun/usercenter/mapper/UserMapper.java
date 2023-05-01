package com.asjun.usercenter.mapper;


import com.asjun.usercenter.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author asjun
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2023-05-01 14:09:10
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




