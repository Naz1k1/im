package com.naz1k1.model.enums;

import lombok.Getter;

@Getter
public enum RCode {
    SUCCESS(200,"操作成功"),
    FAILURE(500,"操作失败"),;

    private final int code;
    private final String message;

    RCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
