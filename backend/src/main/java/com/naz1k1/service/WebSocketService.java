package com.naz1k1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naz1k1.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * WebSocket服务类
 * 负责管理WebSocket会话和消息推送
 */
@Slf4j
@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 推送消息给指定用户
     */
    public void pushMessageToUser(String userId, MessageEvent messageEvent) {
        try {
            // 检查用户是否在线
            String onlineKey = "im:online:" + userId;
            Boolean isOnline = redisTemplate.hasKey(onlineKey);
            
            if (Boolean.TRUE.equals(isOnline)) {
                // 推送到用户专用频道
                String destination = "/user/" + userId + "/queue/messages";
                messagingTemplate.convertAndSendToUser(userId, "/queue/messages", messageEvent);
                
                log.info("WebSocket消息推送成功: userId={}, messageId={}", userId, messageEvent.getMessageId());
            } else {
                log.warn("用户不在线，无法推送WebSocket消息: userId={}", userId);
            }
            
        } catch (Exception e) {
            log.error("WebSocket消息推送失败: userId={}", userId, e);
        }
    }

    /**
     * 推送消息给所有在线用户
     */
    public void pushMessageToAll(MessageEvent messageEvent) {
        try {
            messagingTemplate.convertAndSend("/topic/public", messageEvent);
            log.info("WebSocket消息已推送给所有用户: messageId={}", messageEvent.getMessageId());
            
        } catch (Exception e) {
            log.error("WebSocket消息推送给所有用户失败: messageId={}", messageEvent.getMessageId(), e);
        }
    }

    /**
     * 推送消息给指定频道
     */
    public void pushMessageToChannel(String channel, MessageEvent messageEvent) {
        try {
            String destination = "/topic/" + channel;
            messagingTemplate.convertAndSend(destination, messageEvent);
            
            log.info("WebSocket消息已推送到频道: channel={}, messageId={}", channel, messageEvent.getMessageId());
            
        } catch (Exception e) {
            log.error("WebSocket消息推送到频道失败: channel={}", channel, e);
        }
    }

    /**
     * 设置用户在线状态
     */
    public void setUserOnline(String userId) {
        try {
            String onlineKey = "im:online:" + userId;
            redisTemplate.opsForValue().set(onlineKey, System.currentTimeMillis());
            redisTemplate.expire(onlineKey, 30, TimeUnit.MINUTES); // 30分钟过期
            
            // 发送用户上线通知
            MessageEvent onlineEvent = MessageEvent.builder()
                .messageId("online_" + System.currentTimeMillis())
                .senderId(userId)
                .content("用户上线")
                .messageType("SYSTEM")
                .eventType("USER_ONLINE")
                .sendTime(java.time.LocalDateTime.now())
                .createTime(java.time.LocalDateTime.now())
                .build();
            
            pushMessageToAll(onlineEvent);
            
            log.info("用户在线状态已设置: userId={}", userId);
            
        } catch (Exception e) {
            log.error("设置用户在线状态失败: userId={}", userId, e);
        }
    }

    /**
     * 设置用户离线状态
     */
    public void setUserOffline(String userId) {
        try {
            String onlineKey = "im:online:" + userId;
            redisTemplate.delete(onlineKey);
            
            // 设置用户最后在线时间
            String lastOnlineKey = "im:last_online:" + userId;
            redisTemplate.opsForValue().set(lastOnlineKey, System.currentTimeMillis());
            redisTemplate.expire(lastOnlineKey, 30, TimeUnit.DAYS);
            
            // 发送用户下线通知
            MessageEvent offlineEvent = MessageEvent.builder()
                .messageId("offline_" + System.currentTimeMillis())
                .senderId(userId)
                .content("用户下线")
                .messageType("SYSTEM")
                .eventType("USER_OFFLINE")
                .sendTime(java.time.LocalDateTime.now())
                .createTime(java.time.LocalDateTime.now())
                .build();
            
            pushMessageToAll(offlineEvent);
            
            log.info("用户离线状态已设置: userId={}", userId);
            
        } catch (Exception e) {
            log.error("设置用户离线状态失败: userId={}", userId, e);
        }
    }

    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(String userId) {
        try {
            String onlineKey = "im:online:" + userId;
            return Boolean.TRUE.equals(redisTemplate.hasKey(onlineKey));
        } catch (Exception e) {
            log.error("检查用户在线状态失败: userId={}", userId, e);
            return false;
        }
    }

    /**
     * 获取在线用户数量
     */
    public long getOnlineUserCount() {
        try {
            // 这里可以根据实际需求实现获取在线用户数量的逻辑
            // 比如通过Redis的SCAN命令统计在线用户
            return 0; // 临时返回0，需要根据实际实现
        } catch (Exception e) {
            log.error("获取在线用户数量失败", e);
            return 0;
        }
    }

    /**
     * 推送系统通知
     */
    public void pushSystemNotification(String userId, String content) {
        try {
            MessageEvent systemEvent = MessageEvent.builder()
                .messageId("system_" + System.currentTimeMillis())
                .senderId("SYSTEM")
                .receiverId(userId)
                .content(content)
                .messageType("SYSTEM")
                .eventType("SYSTEM_NOTIFICATION")
                .sendTime(java.time.LocalDateTime.now())
                .createTime(java.time.LocalDateTime.now())
                .build();
            
            pushMessageToUser(userId, systemEvent);
            
            log.info("系统通知已推送: userId={}, content={}", userId, content);
            
        } catch (Exception e) {
            log.error("推送系统通知失败: userId={}", userId, e);
        }
    }
} 