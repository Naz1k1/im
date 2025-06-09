package com.naz1k1.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private int code;    // 状态码
    private String msg;  // 提示信息
    private T data;      // 响应数据

    // 成功（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 成功（带数据）
    public static <T> Result<T> success(T data) {
        return restResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    // 失败（默认错误码）
    public static <T> Result<T> failed() {
        return failed(ResultCode.FAILED.getMessage());
    }

    // 失败（自定义错误信息）
    public static <T> Result<T> failed(String msg) {
        return failed(ResultCode.FAILED.getCode(), msg);
    }

    // 失败（指定错误码和错误信息）
    public static <T> Result<T> failed(int code, String msg) {
        return restResult(code, msg, null);
    }

    // 失败（使用枚举）
    public static <T> Result<T> failed(ResultCode resultCode) {
        return restResult(resultCode.getCode(), resultCode.getMessage(), null);
    }

    // 封装返回值
    private static <T> Result<T> restResult(int code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
