package com.naz1k1.service;

import com.naz1k1.config.RocketMQConfig;
import com.naz1k1.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 消息持久化消费者
 * 负责将消息保存到MongoDB
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = RocketMQConfig.MESSAGE_PERSISTENCE_TOPIC,
    consumerGroup = RocketMQConfig.CONSUMER_GROUP_PERSISTENCE
)
public class MessagePersistenceConsumer implements RocketMQListener<MessageEvent> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onMessage(MessageEvent messageEvent) {
        try {
            log.info("收到消息持久化事件: {}", messageEvent.getMessageId());
            
            // 设置创建时间
            if (messageEvent.getCreateTime() == null) {
                messageEvent.setCreateTime(LocalDateTime.now());
            }
            
            // 保存消息到MongoDB
            mongoTemplate.save(messageEvent, "messages");
            
            log.info("消息已持久化到MongoDB: {}", messageEvent.getMessageId());
            
        } catch (Exception e) {
            log.error("消息持久化失败: {}", messageEvent.getMessageId(), e);
            // 这里可以添加重试逻辑或者死信队列处理
            throw new RuntimeException("消息持久化失败", e);
        }
    }
} 