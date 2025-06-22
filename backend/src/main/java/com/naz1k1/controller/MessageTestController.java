package com.naz1k1.controller;

import com.naz1k1.service.MessageProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息测试控制器
 * 用于测试RocketMQ消息发送功能
 */
@Slf4j
@RestController
@RequestMapping("/api/message/test")
public class MessageTestController {

    @Autowired
    private MessageProcessingService messageProcessingService;

    /**
     * 发送测试消息
     */
    @PostMapping("/send")
    public Map<String, Object> sendTestMessage(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String senderId = request.get("senderId");
            String receiverId = request.get("receiverId");
            String content = request.get("content");
            String messageType = request.getOrDefault("messageType", "TEXT");
            String source = request.getOrDefault("source", "WEB");

            messageProcessingService.processMessage(senderId, receiverId, content, messageType, source);

            response.put("success", true);
            response.put("message", "消息发送成功");
            log.info("测试消息发送成功: senderId={}, receiverId={}", senderId, receiverId);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "消息发送失败: " + e.getMessage());
            log.error("测试消息发送失败", e);
        }

        return response;
    }

    /**
     * 异步发送测试消息
     */
    @PostMapping("/send-async")
    public Map<String, Object> sendTestMessageAsync(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String senderId = request.get("senderId");
            String receiverId = request.get("receiverId");
            String content = request.get("content");
            String messageType = request.getOrDefault("messageType", "TEXT");
            String source = request.getOrDefault("source", "WEB");

            messageProcessingService.processMessageAsync(senderId, receiverId, content, messageType, source);

            response.put("success", true);
            response.put("message", "异步消息发送成功");
            log.info("异步测试消息发送成功: senderId={}, receiverId={}", senderId, receiverId);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "异步消息发送失败: " + e.getMessage());
            log.error("异步测试消息发送失败", e);
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
        response.put("service", "Message Test Service");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
} 