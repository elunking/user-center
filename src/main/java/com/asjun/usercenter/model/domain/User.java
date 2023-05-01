package com.asjun.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    @TableField(value = "userAccount")
    private String userAccount;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户头像
     */
    @TableField(value = "avatarUrl")
    private String avatarUrl;

    /**
     * 性别
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 密码
     */
    @TableField(value = "userPassword")
    private String userPassword;

    /**
     * 电话号码
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户状态
     */
    @TableField(value = "userStatus")
    private Integer userStatus;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 用户角色0-普通用户1，-管理官
     */
    @TableField(value = "userRole")
    private Integer userRole;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}