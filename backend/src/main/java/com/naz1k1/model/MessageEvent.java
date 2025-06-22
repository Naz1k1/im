package com.naz1k1.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息事件模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 接收者ID
     */
    private String receiverId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型：TEXT, IMAGE, FILE, VOICE, VIDEO
     */
    private String messageType;

    /**
     * 消息状态：SENDING, SENT, DELIVERED, READ
     */
    private String status;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 消息来源：WEB, MOBILE, DESKTOP
     */
    private String source;

    /**
     * 扩展字段
     */
    private String extra;

    /**
     * 事件类型：MESSAGE_SEND, MESSAGE_DELIVER, MESSAGE_READ
     */
    private String eventType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 