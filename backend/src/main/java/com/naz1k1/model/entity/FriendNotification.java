package com.naz1k1.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("friend_notification")
public class FriendNotification extends BaseEntity {
    private Long receiverId;
    private Long senderId;
    private int type;
    private String content;
}
