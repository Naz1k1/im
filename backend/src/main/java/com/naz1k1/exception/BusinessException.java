package com.naz1k1.exception;

import com.naz1k1.model.enums.RCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public BusinessException(RCode rCode) {
        super(rCode.getMessage());
        this.code = rCode.getCode();
    }
}
