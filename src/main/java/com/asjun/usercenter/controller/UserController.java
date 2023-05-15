package com.asjun.usercenter.controller;

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
    public Long userRegister(@RequestBody UserRegisterRequest registerRequest) {
        if (registerRequest == null) return null;
        String userAccount = registerRequest.getUserAccount();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        if (loginRequest == null) return null;
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (attribute == null) return null;
        User userObj = (User) attribute;
        Long id = userObj.getId();
        User user = userService.getById(id);
        return userService.getSaftyUser(user);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) return null;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSaftyUser(user)).toList();
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) return false;
        if (id < 0) return false;
        return userService.removeById(id);
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
