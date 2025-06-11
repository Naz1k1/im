package com.naz1k1.model.enums;

import lombok.Getter;

@Getter
public enum RCode {
    SUCCESS(200,"操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),
    VALIDATION_FAILED(422, "参数验证失败"),
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),
    ;
    private final int code;
    private final String message;

    RCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
