package com.asjun.usercenter.common;

/**
 * 结果处理工具
 * @author asjun
 */
public class ResultUtils {
    /**
     * 成功
     * @param data 成功返回的数据
     * @return 返回封装的响应值
     * @param <T> 响应值中的数据体的类型
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"OK","");
    }

    /**
     * 失败
     * @param errorCode 封装错误码
     * @return 返回封装的响应值
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode);
    }

    /**
     * 失败
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse(errorCode.getCode(),message,description);
    }

    /**
     * 失败
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse(errorCode.getCode(),errorCode.getMessage(),description);
    }

    public static BaseResponse error(int code,String message,String description){
        return new BaseResponse(code,message,description);
    }




}
