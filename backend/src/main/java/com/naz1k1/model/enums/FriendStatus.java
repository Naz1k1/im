package com.naz1k1.model.enums;

import lombok.Getter;

@Getter
public enum FriendStatus {
    NOT_FRIEND(0,"待添加"),
    PENDING(1,"待处理"),
    NORMAL(2,"正常状态"),
    DECLINED(3,"已拒绝"),
    DELETED(4,"已删除"),
    BLOCKED(5,"已拉黑");

    private final String description;
    private final Integer value;

    public static FriendStatus fromValue(Integer value) {
        for (FriendStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    FriendStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
