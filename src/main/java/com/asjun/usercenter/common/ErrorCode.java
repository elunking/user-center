package com.asjun.usercenter.common;

/**
 * 错误码
 * @author asjun
 */
public enum ErrorCode {
    PARAMS_ERROR(40000, "请求参数错误", ""),
    SYSTEM_ERROR(50001,"系统错误",""),
    NULL_ERROR(40001,"请求参数为空",""),
    NOT_LOGIN(40100, "未登录", ""),
    NOT_AUTH(40101, "未授权", "");
    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
