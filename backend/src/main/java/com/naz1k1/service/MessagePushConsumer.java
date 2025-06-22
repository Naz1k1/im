package com.naz1k1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naz1k1.config.RocketMQConfig;
import com.naz1k1.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息推送消费者
 * 负责推送消息给在线用户
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = RocketMQConfig.MESSAGE_PUSH_TOPIC,
    consumerGroup = RocketMQConfig.CONSUMER_GROUP_PUSH
)
public class MessagePushConsumer implements RocketMQListener<MessageEvent> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    // WebSocket会话存储（实际项目中可能需要更复杂的会话管理）
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    @Override
    public void onMessage(MessageEvent messageEvent) {
        try {
            log.info("收到消息推送事件: {}", messageEvent.getMessageId());
            
            String receiverId = messageEvent.getReceiverId();
            
            // 检查用户是否在线
            String onlineKey = "im:online:" + receiverId;
            Boolean isOnline = redisTemplate.hasKey(onlineKey);
            
            if (Boolean.TRUE.equals(isOnline)) {
                // 用户在线，通过WebSocket推送消息
                pushMessageToUser(receiverId, messageEvent);
                log.info("消息已推送给在线用户: {}", receiverId);
            } else {
                // 用户离线，可以发送推送通知
                sendPushNotification(receiverId, messageEvent);
                log.info("用户离线，已发送推送通知: {}", receiverId);
            }
            
        } catch (Exception e) {
            log.error("消息推送失败: {}", messageEvent.getMessageId(), e);
            // 这里可以添加重试逻辑或者死信队列处理
            throw new RuntimeException("消息推送失败", e);
        }
    }

    /**
     * 通过WebSocket推送消息给用户
     */
    private void pushMessageToUser(String userId, MessageEvent messageEvent) {
        try {
            WebSocketSession session = SESSIONS.get(userId);
            if (session != null && session.isOpen()) {
                String messageJson = objectMapper.writeValueAsString(messageEvent);
                session.sendMessage(new TextMessage(messageJson));
                log.info("WebSocket消息推送成功: userId={}", userId);
            } else {
                log.warn("用户WebSocket会话不存在或已关闭: userId={}", userId);
            }
        } catch (IOException e) {
            log.error("WebSocket消息推送失败: userId={}", userId, e);
        }
    }

    /**
     * 发送推送通知（可以集成第三方推送服务）
     */
    private void sendPushNotification(String userId, MessageEvent messageEvent) {
        try {
            // 这里可以集成第三方推送服务，如极光推送、个推等
            // 示例：发送到推送服务队列
            String pushKey = "im:push_notification:" + userId;
            redisTemplate.opsForList().rightPush(pushKey, messageEvent);
            redisTemplate.expire(pushKey, 7, java.util.concurrent.TimeUnit.DAYS);
            
            log.info("推送通知已加入队列: userId={}", userId);
        } catch (Exception e) {
            log.error("发送推送通知失败: userId={}", userId, e);
        }
    }

    /**
     * 注册WebSocket会话
     */
    public static void registerSession(String userId, WebSocketSession session) {
        SESSIONS.put(userId, session);
        log.info("WebSocket会话已注册: userId={}", userId);
    }

    /**
     * 移除WebSocket会话
     */
    public static void removeSession(String userId) {
        SESSIONS.remove(userId);
        log.info("WebSocket会话已移除: userId={}", userId);
    }
} 