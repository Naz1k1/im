package com.naz1k1.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendRequestVO {
    private Long id;
    private Long userId;
    private Long friendId;
    private String remark;
    private int status;
    private LocalDateTime createdAt;
}
