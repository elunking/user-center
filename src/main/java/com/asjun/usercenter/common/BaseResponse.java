package com.asjun.usercenter.common;

import lombok.Data;

/**
 * 封装的响应体
 * @param <T> 数据格式
 * @author asjun
 */
@Data
public class BaseResponse<T> {
    private int code;
    private T data;
    private String message;
    private String description;
    private ErrorCode errorCode;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }
    public BaseResponse(int code, String message, String description) {
        this(code, null, message,description);
    }


    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
