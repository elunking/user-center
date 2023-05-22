package com.asjun.usercenter.controller;

import com.asjun.usercenter.common.BaseResponse;
import com.asjun.usercenter.common.ErrorCode;
import com.asjun.usercenter.common.ResultUtils;
import com.asjun.usercenter.exception.BusinessException;
import com.asjun.usercenter.model.domain.User;
import com.asjun.usercenter.model.request.UserLoginRequest;
import com.asjun.usercenter.model.request.UserRegisterRequest;
import com.asjun.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.asjun.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.asjun.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户控制层
 *
 * @author asjun
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest registerRequest) {
        if (registerRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String userAccount = registerRequest.getUserAccount();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        String planetId = registerRequest.getPlanetId();
        long id = userService.userRegister(userAccount, userPassword, checkPassword, planetId);
        return ResultUtils.success(id);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        if (loginRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (attribute == null) throw new BusinessException(ErrorCode.NOT_LOGIN,"未登录");
        User userObj = (User) attribute;
        Long id = userObj.getId();
        User user = userService.getById(id);
        return ResultUtils.success(user);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) throw new BusinessException(ErrorCode.NOT_AUTH,"该用户不是管理员");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> users = userList.stream().map(user -> userService.getSaftyUser(user)).toList();
        return ResultUtils.success(users);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) throw new BusinessException(ErrorCode.NULL_ERROR,"该用户不是管理员");
        if (id < 0) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 用户注销请求
     * @param request 当前注销请求
     * @return 成功返回1
     */

    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest request){
        return userService.userLogout(request);
    }


    /**
     * 是否是管理员
     *
     * @param request 请求体
     * @return 返回一个boolean判断是否为管理员
     */
    public boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
