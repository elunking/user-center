package com.asjun.usercenter.service.impl;

import com.asjun.usercenter.mapper.UserMapper;
import com.asjun.usercenter.model.domain.User;
import com.asjun.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author asjun
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-05-01 14:09:10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String SALT = "ajsun";
    private static final String USER_LOGIN_STATE = "userLoginState";

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册接口实现
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 检验密码
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1.校验

        //判空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) return -1;
        //账户不能小于4位
        if (userAccount.length() < 4) return -1;
        //密码长度不能小于8位
        if (userPassword.length() < 8) return -1;

        //检查账户是否包含特殊字符
        String pattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(userAccount);
        if (matcher.find()) return -1;

        //检验密码和校验密码是否相同
        if (!userPassword.equals(checkPassword)) return -1;

        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long result = userMapper.selectCount(queryWrapper);
        if (result > 0) return -1;

        //3. 对密码进行加密
        String digestedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //4. 保存用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(digestedPassword);
        int insert = userMapper.insert(user);
        if (!(insert > 0)) return -1;
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        //判空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) return null;
        //账户不能小于4位
        if (userAccount.length() < 4) return null;
        //密码长度不能小于8位
        if (userPassword.length() < 8) return null;

        //检查账户是否包含特殊字符
        String pattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(userAccount);
        if (matcher.find()) return null;

        //对密码进行加密;
        String digestedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 2.校验密码是否输入正确，要和数据库中的密文密码区对比
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",digestedPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null) return null;

        // 3.对用户信息进行脱敏
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setUsername(user.getUsername());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setUserRole(user.getUserRole());

        // 4.记录用户的登录态，将其存到服务器上（session）
        request.getSession().setAttribute(USER_LOGIN_STATE,safeUser);

        // 5.返回脱敏后的用户信息
        return safeUser;

    }
}




