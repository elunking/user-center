package com.asjun.usercenter.service.impl;

import com.asjun.usercenter.common.ErrorCode;
import com.asjun.usercenter.exception.BusinessException;
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

import static com.asjun.usercenter.constant.UserConstant.*;

/**
 * @author asjun
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-05-01 14:09:10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String SALT = "ajsun";

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册接口实现
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 检验密码
     * @param planetId      星球编号
     * @return 返回用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetId) {
        // 1.校验

        //判空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetId)) throw new BusinessException(ErrorCode.PARAMS_ERROR);;
        //账户不能小于4位
        if (userAccount.length() < 4) throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户应该大于4为");
        //密码长度不能小于8位
        if (userPassword.length() < 8) throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码应该大于8位");
        //星球编号不能大于5位
        if(planetId.length() > 5) throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号应该小于5位！");

        //检查账户是否包含特殊字符
        String pattern = "[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(userAccount);
        if (matcher.find()) throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户包含特殊字符！");;

        //检验密码和校验密码是否相同
        if (!userPassword.equals(checkPassword)) throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不相同");;

        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long result = userMapper.selectCount(queryWrapper);
        if (result > 0) throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能重复");;

        //星球编号不能重复
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("planetId", planetId);
        Long count = userMapper.selectCount(queryWrapper1);
        if (count > 0) throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号应该小于5位");;

        //3. 对密码进行加密
        String digestedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //4. 保存用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(digestedPassword);
        user.setPlanetId(planetId);
        int insert = userMapper.insert(user);
        if (!(insert > 0)) throw new BusinessException(ErrorCode.PARAMS_ERROR,"注册失败！");;
        return user.getId();
    }


    /**
     * 用户登录接口实现
     *
     * @param userAccount  登录账户
     * @param userPassword 登录密码
     * @param request      用户请求
     * @return 返回脱敏信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        //判空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误");;
        //账户不能小于4位
        if (userAccount.length() < 4) throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户应该大于4为");;
        //密码长度不能小于8位
        if (userPassword.length() < 8) throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码应该大于8为");;

        //检查账户是否包含特殊字符
        String pattern = "[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(userAccount);
        if (matcher.find()) throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户包含特殊字符");;

        //对密码进行加密;
        String digestedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 2.校验密码是否输入正确，要和数据库中的密文密码区对比
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount).eq("userPassword", digestedPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码错误！");;

        // 3.对用户信息进行脱敏
        User safeUser = getSaftyUser(user);

        // 4.记录用户的登录态，将其存到服务器上（session）
        request.getSession().setAttribute(USER_LOGIN_STATE, safeUser);

        // 5.返回脱敏后的用户信息
        return safeUser;
    }

    /**
     * 用户数据脱敏接口实现
     *
     * @param originUser 源用户数据
     * @return 返回脱敏后的数据
     */
    @Override
    public User getSaftyUser(User originUser) {
        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setUsername(originUser.getUsername());
        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setUserRole(originUser.getUserRole());
        safeUser.setCreateTime(originUser.getCreateTime());
        safeUser.setPlanetId(originUser.getPlanetId());
        return safeUser;
    }

    /**
     * 注销接口实现
     *
     * @param request 当前注销请求
     * @return 成功返回1
     */
    @Override
    public Integer userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




