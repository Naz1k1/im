package com.naz1k1.model.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private String messageId;          // 消息ID
    private Integer messageType;       // 消息类型：1-私聊消息，2-群聊消息
    private Long senderId;            // 发送者ID
    private Long receiverId;          // 接收者ID（私聊时为用户ID，群聊时为群ID）
    private String content;    // 消息内容
    private Long timestamp;           // 消息时间戳
} 