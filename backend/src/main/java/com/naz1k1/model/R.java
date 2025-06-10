package com.naz1k1.model;

import com.naz1k1.model.enums.RCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应封装类
 * @param <T>
 */
@Data
public class R<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    //成功带数据
    public static <T> R<T> success(T data) {
        return restR(RCode.SUCCESS, data);
    }

    //成功带数据
    public static <T> R<T> success() {
        return restR(RCode.SUCCESS, null);
    }

    public static <T> R<T> failure() {
        return restR(RCode.FAILURE, null);
    }

    //封装返回值
    private static <T> R<T> restR(RCode code, T data) {
        R<T> r = new R<>();
        r.code = code.getCode();
        r.message = code.getMessage();
        r.data = data;
        return r;
    }
}
