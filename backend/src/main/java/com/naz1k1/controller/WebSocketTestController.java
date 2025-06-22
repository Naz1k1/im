package com.naz1k1.controller;

import com.naz1k1.model.MessageEvent;
import com.naz1k1.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * WebSocket测试控制器
 * 用于测试WebSocket消息推送功能
 */
@Slf4j
@RestController
@RequestMapping("/api/websocket/test")
public class WebSocketTestController {

    @Autowired
    private WebSocketService webSocketService;

    /**
     * 推送消息给指定用户
     */
    @PostMapping("/push-to-user")
    public Map<String, Object> pushToUser(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String userId = request.get("userId");
            String content = request.get("content");
            
            if (userId == null || content == null) {
                response.put("success", false);
                response.put("message", "userId和content不能为空");
                return response;
            }
            
            MessageEvent messageEvent = MessageEvent.builder()
                .messageId(UUID.randomUUID().toString())
                .senderId("SYSTEM")
                .receiverId(userId)
                .content(content)
                .messageType("TEST")
                .eventType("TEST_MESSAGE")
                .sendTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .build();
            
            webSocketService.pushMessageToUser(userId, messageEvent);
            
            response.put("success", true);
            response.put("message", "消息推送成功");
            response.put("messageId", messageEvent.getMessageId());
            
            log.info("WebSocket测试消息推送成功: userId={}, content={}", userId, content);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "消息推送失败: " + e.getMessage());
            log.error("WebSocket测试消息推送失败", e);
        }
        
        return response;
    }

    /**
     * 推送消息给所有用户
     */
    @PostMapping("/push-to-all")
    public Map<String, Object> pushToAll(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String content = request.get("content");
            
            if (content == null) {
                response.put("success", false);
                response.put("message", "content不能为空");
                return response;
            }
            
            MessageEvent messageEvent = MessageEvent.builder()
                .messageId(UUID.randomUUID().toString())
                .senderId("SYSTEM")
                .content(content)
                .messageType("BROADCAST")
                .eventType("BROADCAST_MESSAGE")
                .sendTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .build();
            
            webSocketService.pushMessageToAll(messageEvent);
            
            response.put("success", true);
            response.put("message", "广播消息推送成功");
            response.put("messageId", messageEvent.getMessageId());
            
            log.info("WebSocket广播消息推送成功: content={}", content);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "广播消息推送失败: " + e.getMessage());
            log.error("WebSocket广播消息推送失败", e);
        }
        
        return response;
    }

    /**
     * 推送消息给指定频道
     */
    @PostMapping("/push-to-channel")
    public Map<String, Object> pushToChannel(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String channel = request.get("channel");
            String content = request.get("content");
            
            if (channel == null || content == null) {
                response.put("success", false);
                response.put("message", "channel和content不能为空");
                return response;
            }
            
            MessageEvent messageEvent = MessageEvent.builder()
                .messageId(UUID.randomUUID().toString())
                .senderId("SYSTEM")
                .content(content)
                .messageType("CHANNEL")
                .eventType("CHANNEL_MESSAGE")
                .sendTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .build();
            
            webSocketService.pushMessageToChannel(channel, messageEvent);
            
            response.put("success", true);
            response.put("message", "频道消息推送成功");
            response.put("messageId", messageEvent.getMessageId());
            
            log.info("WebSocket频道消息推送成功: channel={}, content={}", channel, content);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "频道消息推送失败: " + e.getMessage());
            log.error("WebSocket频道消息推送失败", e);
        }
        
        return response;
    }

    /**
     * 设置用户在线状态
     */
    @PostMapping("/set-online")
    public Map<String, Object> setOnline(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String userId = request.get("userId");
            
            if (userId == null) {
                response.put("success", false);
                response.put("message", "userId不能为空");
                return response;
            }
            
            webSocketService.setUserOnline(userId);
            
            response.put("success", true);
            response.put("message", "用户在线状态设置成功");
            
            log.info("用户在线状态设置成功: userId={}", userId);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "设置用户在线状态失败: " + e.getMessage());
            log.error("设置用户在线状态失败", e);
        }
        
        return response;
    }

    /**
     * 设置用户离线状态
     */
    @PostMapping("/set-offline")
    public Map<String, Object> setOffline(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String userId = request.get("userId");
            
            if (userId == null) {
                response.put("success", false);
                response.put("message", "userId不能为空");
                return response;
            }
            
            webSocketService.setUserOffline(userId);
            
            response.put("success", true);
            response.put("message", "用户离线状态设置成功");
            
            log.info("用户离线状态设置成功: userId={}", userId);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "设置用户离线状态失败: " + e.getMessage());
            log.error("设置用户离线状态失败", e);
        }
        
        return response;
    }

    /**
     * 检查用户在线状态
     */
    @GetMapping("/check-online/{userId}")
    public Map<String, Object> checkOnline(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isOnline = webSocketService.isUserOnline(userId);
            
            response.put("success", true);
            response.put("userId", userId);
            response.put("isOnline", isOnline);
            
            log.info("用户在线状态检查: userId={}, isOnline={}", userId, isOnline);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "检查用户在线状态失败: " + e.getMessage());
            log.error("检查用户在线状态失败: userId={}", userId, e);
        }
        
        return response;
    }

    /**
     * 推送系统通知
     */
    @PostMapping("/system-notification")
    public Map<String, Object> systemNotification(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String userId = request.get("userId");
            String content = request.get("content");
            
            if (userId == null || content == null) {
                response.put("success", false);
                response.put("message", "userId和content不能为空");
                return response;
            }
            
            webSocketService.pushSystemNotification(userId, content);
            
            response.put("success", true);
            response.put("message", "系统通知推送成功");
            
            log.info("系统通知推送成功: userId={}, content={}", userId, content);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统通知推送失败: " + e.getMessage());
            log.error("系统通知推送失败", e);
        }
        
        return response;
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "WebSocket Test Service");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
} 