package com.naz1k1.model.enums;

import lombok.Getter;

@Getter
public enum FriendStatus {
    PENDING(0, "待处理"),
    ACCEPTED(1, "已好友"),
    REJECTED(2, "已拒绝"),
    BLOCKED(3, "已拉黑");

    private final int code;
    private final String desc;

    FriendStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
} 