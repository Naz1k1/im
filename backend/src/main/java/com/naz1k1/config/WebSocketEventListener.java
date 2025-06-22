package com.naz1k1.config;

import com.naz1k1.service.MessagePushConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.TimeUnit;

/**
 * WebSocket事件监听器
 * 处理WebSocket连接和断开事件
 */
@Slf4j
@Component
public class WebSocketEventListener {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 处理WebSocket连接事件
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        try {
            String sessionId = headerAccessor.getSessionId();
            log.info("WebSocket连接已建立: sessionId={}", sessionId);
            
            // 这里可以添加连接后的初始化逻辑
            // 比如设置用户在线状态、加载未读消息等
            
        } catch (Exception e) {
            log.error("处理WebSocket连接事件失败: sessionId={}", headerAccessor.getSessionId(), e);
        }
    }

    /**
     * 处理WebSocket断开事件
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        try {
            String sessionId = headerAccessor.getSessionId();
            String userId = (String) headerAccessor.getSessionAttributes().get("userId");
            
            if (userId != null) {
                // 移除WebSocket会话
                MessagePushConsumer.removeSession(userId);
                
                // 设置用户离线状态
                String onlineKey = "im:online:" + userId;
                redisTemplate.delete(onlineKey);
                
                // 设置用户最后在线时间
                String lastOnlineKey = "im:last_online:" + userId;
                redisTemplate.opsForValue().set(lastOnlineKey, System.currentTimeMillis());
                redisTemplate.expire(lastOnlineKey, 30, TimeUnit.DAYS);
                
                log.info("用户已断开WebSocket连接: userId={}, sessionId={}", userId, sessionId);
            } else {
                log.warn("WebSocket断开连接但未找到用户ID: sessionId={}", sessionId);
            }
            
        } catch (Exception e) {
            log.error("处理WebSocket断开事件失败: sessionId={}", headerAccessor.getSessionId(), e);
        }
    }
} 