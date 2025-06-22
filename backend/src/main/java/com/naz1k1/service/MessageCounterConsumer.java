package com.naz1k1.service;

import com.naz1k1.config.RocketMQConfig;
import com.naz1k1.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 消息计数器消费者
 * 负责更新Redis中的消息计数器
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = RocketMQConfig.MESSAGE_COUNTER_TOPIC,
    consumerGroup = RocketMQConfig.CONSUMER_GROUP_COUNTER
)
public class MessageCounterConsumer implements RocketMQListener<MessageEvent> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(MessageEvent messageEvent) {
        try {
            log.info("收到消息计数器事件: {}", messageEvent.getMessageId());
            
            String receiverId = messageEvent.getReceiverId();
            String senderId = messageEvent.getSenderId();
            
            // 更新接收者的未读消息计数
            String unreadKey = "im:unread:" + receiverId + ":" + senderId;
            redisTemplate.opsForValue().increment(unreadKey);
            redisTemplate.expire(unreadKey, 30, TimeUnit.DAYS); // 设置过期时间
            
            // 更新总未读消息计数
            String totalUnreadKey = "im:total_unread:" + receiverId;
            redisTemplate.opsForValue().increment(totalUnreadKey);
            redisTemplate.expire(totalUnreadKey, 30, TimeUnit.DAYS);
            
            // 更新最后消息时间
            String lastMessageKey = "im:last_message:" + receiverId + ":" + senderId;
            redisTemplate.opsForValue().set(lastMessageKey, messageEvent.getSendTime());
            redisTemplate.expire(lastMessageKey, 30, TimeUnit.DAYS);
            
            log.info("消息计数器已更新: receiverId={}, senderId={}", receiverId, senderId);
            
        } catch (Exception e) {
            log.error("消息计数器更新失败: {}", messageEvent.getMessageId(), e);
            // 这里可以添加重试逻辑或者死信队列处理
            throw new RuntimeException("消息计数器更新失败", e);
        }
    }
} 