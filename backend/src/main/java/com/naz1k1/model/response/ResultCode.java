package com.naz1k1.model.response;

import lombok.Getter;

@Getter
public enum ResultCode {
    /* 成功状态码 */
    SUCCESS(200, "操作成功"),

    /* 通用错误码 5xx */
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(501, "参数校验失败"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),

    /* 业务错误码 */
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    TOKEN_INVALID(1003, "Token无效或过期");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
