package com.naz1k1.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("friend_relationship")
public class FriendRelationship extends BaseEntity {
    private Long friendId;
    private Long userId;
    private int status;
    private String remark;
}
