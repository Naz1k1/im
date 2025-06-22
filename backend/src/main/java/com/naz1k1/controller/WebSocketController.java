package com.naz1k1.controller;

import com.naz1k1.service.MessageProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * WebSocket消息处理控制器
 * 处理客户端通过WebSocket发送的消息
 */
@Slf4j
@Controller
public class WebSocketController {

    @Autowired
    private MessageProcessingService messageProcessingService;

    /**
     * 处理用户发送的消息
     * 客户端发送消息到 /app/chat.sendMessage
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Map<String, Object> sendMessage(@Payload Map<String, Object> chatMessage, 
                                          SimpMessageHeaderAccessor headerAccessor) {
        try {
            String senderId = (String) chatMessage.get("senderId");
            String receiverId = (String) chatMessage.get("receiverId");
            String content = (String) chatMessage.get("content");
            String messageType = (String) chatMessage.getOrDefault("messageType", "TEXT");
            
            // 处理消息
            messageProcessingService.processMessage(senderId, receiverId, content, messageType, "WEBSOCKET");
            
            // 返回确认消息
            chatMessage.put("status", "SENT");
            chatMessage.put("timestamp", System.currentTimeMillis());
            
            log.info("WebSocket消息已处理: senderId={}, receiverId={}", senderId, receiverId);
            
        } catch (Exception e) {
            log.error("WebSocket消息处理失败", e);
            chatMessage.put("status", "ERROR");
            chatMessage.put("error", e.getMessage());
        }
        
        return chatMessage;
    }

    /**
     * 处理用户加入聊天室
     * 客户端发送消息到 /app/chat.addUser
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Map<String, Object> addUser(@Payload Map<String, Object> chatMessage, 
                                      SimpMessageHeaderAccessor headerAccessor) {
        try {
            // 添加用户到WebSocket会话
            String userId = (String) chatMessage.get("senderId");
            headerAccessor.getSessionAttributes().put("userId", userId);
            
            // 设置用户在线状态
            chatMessage.put("type", "JOIN");
            chatMessage.put("timestamp", System.currentTimeMillis());
            
            log.info("用户已加入WebSocket聊天: userId={}", userId);
            
        } catch (Exception e) {
            log.error("用户加入WebSocket聊天失败", e);
            chatMessage.put("status", "ERROR");
            chatMessage.put("error", e.getMessage());
        }
        
        return chatMessage;
    }

    /**
     * 处理用户离开聊天室
     * 客户端发送消息到 /app/chat.leaveUser
     */
    @MessageMapping("/chat.leaveUser")
    @SendTo("/topic/public")
    public Map<String, Object> leaveUser(@Payload Map<String, Object> chatMessage, 
                                        SimpMessageHeaderAccessor headerAccessor) {
        try {
            String userId = (String) chatMessage.get("senderId");
            
            // 设置用户离线状态
            chatMessage.put("type", "LEAVE");
            chatMessage.put("timestamp", System.currentTimeMillis());
            
            log.info("用户已离开WebSocket聊天: userId={}", userId);
            
        } catch (Exception e) {
            log.error("用户离开WebSocket聊天失败", e);
            chatMessage.put("status", "ERROR");
            chatMessage.put("error", e.getMessage());
        }
        
        return chatMessage;
    }

    /**
     * 处理私聊消息
     * 客户端发送消息到 /app/chat.privateMessage
     */
    @MessageMapping("/chat.privateMessage")
    public void sendPrivateMessage(@Payload Map<String, Object> chatMessage) {
        try {
            String senderId = (String) chatMessage.get("senderId");
            String receiverId = (String) chatMessage.get("receiverId");
            String content = (String) chatMessage.get("content");
            String messageType = (String) chatMessage.getOrDefault("messageType", "TEXT");
            
            // 处理私聊消息
            messageProcessingService.processMessage(senderId, receiverId, content, messageType, "WEBSOCKET");
            
            log.info("WebSocket私聊消息已处理: senderId={}, receiverId={}", senderId, receiverId);
            
        } catch (Exception e) {
            log.error("WebSocket私聊消息处理失败", e);
        }
    }
} 