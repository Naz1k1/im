package com.naz1k1.model.response;

import com.naz1k1.model.enums.FriendStatus;
import lombok.Data;

@Data
public class UserSearchVO {
    private Long userId;         // 用户ID
    private String username;     // 用户名
    private String nickname;     // 昵称
    private String avatar;       // 头像URL
    private FriendStatus status;    // 是否已经是好友
    private String remark;       // 如果是好友，显示备注名
}
