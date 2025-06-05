package com.naz1k1.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoVO {
    private String username;
    private String nickname;
    private String avatar;
    private LocalDateTime createdAt;
}
