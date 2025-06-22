package com.naz1k1.service;

import com.naz1k1.config.RocketMQConfig;
import com.naz1k1.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * 消息生产者服务
 */
@Slf4j
@Service
public class MessageProducerService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送消息到主消息主题
     */
    public void sendMessage(MessageEvent messageEvent) {
        try {
            rocketMQTemplate.syncSend(RocketMQConfig.MESSAGE_TOPIC, 
                MessageBuilder.withPayload(messageEvent).build());
            log.info("消息已发送到主主题: {}", messageEvent.getMessageId());
        } catch (Exception e) {
            log.error("发送消息到主主题失败: {}", messageEvent.getMessageId(), e);
            throw new RuntimeException("消息发送失败", e);
        }
    }

    /**
     * 发送消息持久化事件
     */
    public void sendPersistenceEvent(MessageEvent messageEvent) {
        try {
            rocketMQTemplate.syncSend(RocketMQConfig.MESSAGE_PERSISTENCE_TOPIC, 
                MessageBuilder.withPayload(messageEvent).build());
            log.info("消息持久化事件已发送: {}", messageEvent.getMessageId());
        } catch (Exception e) {
            log.error("发送消息持久化事件失败: {}", messageEvent.getMessageId(), e);
            throw new RuntimeException("消息持久化事件发送失败", e);
        }
    }

    /**
     * 发送消息计数器更新事件
     */
    public void sendCounterEvent(MessageEvent messageEvent) {
        try {
            rocketMQTemplate.syncSend(RocketMQConfig.MESSAGE_COUNTER_TOPIC, 
                MessageBuilder.withPayload(messageEvent).build());
            log.info("消息计数器事件已发送: {}", messageEvent.getMessageId());
        } catch (Exception e) {
            log.error("发送消息计数器事件失败: {}", messageEvent.getMessageId(), e);
            throw new RuntimeException("消息计数器事件发送失败", e);
        }
    }

    /**
     * 发送消息推送事件
     */
    public void sendPushEvent(MessageEvent messageEvent) {
        try {
            rocketMQTemplate.syncSend(RocketMQConfig.MESSAGE_PUSH_TOPIC, 
                MessageBuilder.withPayload(messageEvent).build());
            log.info("消息推送事件已发送: {}", messageEvent.getMessageId());
        } catch (Exception e) {
            log.error("发送消息推送事件失败: {}", messageEvent.getMessageId(), e);
            throw new RuntimeException("消息推送事件发送失败", e);
        }
    }

} 