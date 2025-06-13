package com.naz1k1.model.enums;

import lombok.Getter;

@Getter
public enum FriendRequestType {
    REQUEST(1, "好友请求"),
    ACCEPT(2, "请求通过"),
    REJECT(3, "请求拒绝");

    private final int code;
    private final String desc;

    FriendRequestType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
} 