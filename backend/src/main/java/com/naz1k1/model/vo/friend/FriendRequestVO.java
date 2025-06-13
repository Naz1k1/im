package com.naz1k1.model.vo.friend;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendRequestVO {
    private Long id;
    private Long senderId;
    private String senderName;
    private String senderAvatar;
    private String content;
    private Integer type;
    private LocalDateTime createdAt;
} 