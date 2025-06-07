package com.naz1k1.entity;

import com.naz1k1.model.response.MessageContent;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
public class PrivateMessage {
    private ObjectId id;
    private Long senderId;
    private Long receiverId;
    private MessageContent content;
    private Integer status;  // 0-未读，1-已读，2-撤回
    private LocalDateTime sendTime;
    private LocalDateTime readTime;
}
