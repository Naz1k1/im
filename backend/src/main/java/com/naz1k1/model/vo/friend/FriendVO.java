package com.naz1k1.model.vo.friend;

import lombok.Data;

@Data
public class FriendVO {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String remark;
    private Integer status;
    private String lastSeen;
} 