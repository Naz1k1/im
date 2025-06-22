package com.naz1k1.service;

import com.naz1k1.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 消息处理服务
 * 负责协调整个消息流程
 */
@Slf4j
@Service
public class MessageProcessingService {

    @Autowired
    private MessageProducerService messageProducerService;

    /**
     * 处理用户发送的消息
     * 1. 发送到主消息主题
     * 2. 发送持久化事件
     * 3. 发送计数器更新事件
     * 4. 发送推送事件
     */
    public void processMessage(String senderId, String receiverId, String content, 
                             String messageType, String source) {
        try {
            // 创建消息事件
            MessageEvent messageEvent = MessageEvent.builder()
                .messageId(UUID.randomUUID().toString())
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .messageType(messageType)
                .status("SENDING")
                .sendTime(LocalDateTime.now())
                .source(source)
                .eventType("MESSAGE_SEND")
                .createTime(LocalDateTime.now())
                .build();

            log.info("开始处理消息: {}", messageEvent.getMessageId());

            // 1. 发送到主消息主题
            messageProducerService.sendMessage(messageEvent);

            // 2. 发送持久化事件
            messageProducerService.sendPersistenceEvent(messageEvent);

            // 3. 发送计数器更新事件
            messageProducerService.sendCounterEvent(messageEvent);

            // 4. 发送推送事件
            messageProducerService.sendPushEvent(messageEvent);

            log.info("消息处理完成: {}", messageEvent.getMessageId());

        } catch (Exception e) {
            log.error("消息处理失败: senderId={}, receiverId={}", senderId, receiverId, e);
            throw new RuntimeException("消息处理失败", e);
        }
    }

    /**
     * 异步处理消息
     */
    public void processMessageAsync(String senderId, String receiverId, String content, 
                                  String messageType, String source) {
        try {
            // 创建消息事件
            MessageEvent messageEvent = MessageEvent.builder()
                .messageId(UUID.randomUUID().toString())
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .messageType(messageType)
                .status("SENDING")
                .sendTime(LocalDateTime.now())
                .source(source)
                .eventType("MESSAGE_SEND")
                .createTime(LocalDateTime.now())
                .build();

            log.info("开始异步处理消息: {}", messageEvent.getMessageId());


            // 同步发送其他事件（可以根据需要改为异步）
            messageProducerService.sendPersistenceEvent(messageEvent);
            messageProducerService.sendCounterEvent(messageEvent);
            messageProducerService.sendPushEvent(messageEvent);

            log.info("异步消息处理完成: {}", messageEvent.getMessageId());

        } catch (Exception e) {
            log.error("异步消息处理失败: senderId={}, receiverId={}", senderId, receiverId, e);
            throw new RuntimeException("异步消息处理失败", e);
        }
    }

    /**
     * 处理消息状态更新
     */
    public void updateMessageStatus(String messageId, String status) {
        try {
            MessageEvent statusEvent = MessageEvent.builder()
                .messageId(messageId)
                .status(status)
                .eventType("MESSAGE_STATUS_UPDATE")
                .createTime(LocalDateTime.now())
                .build();

            // 发送状态更新事件
            messageProducerService.sendMessage(statusEvent);

            log.info("消息状态更新: messageId={}, status={}", messageId, status);

        } catch (Exception e) {
            log.error("消息状态更新失败: messageId={}", messageId, e);
            throw new RuntimeException("消息状态更新失败", e);
        }
    }
} 