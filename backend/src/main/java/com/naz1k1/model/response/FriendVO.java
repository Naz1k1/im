package com.naz1k1.model.response;

import lombok.Data;

@Data
public class FriendVO {
    private Long id;          // 好友关系ID
    private Long friendId;    // 好友用户ID
    private String username;  // 好友用户名
    private String nickname;  // 好友昵称
    private String avatar;    // 好友头像
    private String remark;    // 好友备注
}