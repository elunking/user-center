package com.asjun.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * json格式请求体
 * @author asjun
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 285491673881683355L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetId;
}
