package com.naz1k1.netty.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naz1k1.mq.producer.MessageProducer;
import com.naz1k1.netty.protocol.ChatMessage;
import com.naz1k1.netty.protocol.ChatMessageType;
import com.naz1k1.service.MessageService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    
    private final ObjectMapper objectMapper;
    private final MessageService messageService;
    private final MessageProducer messageProducer;
    private final RedisTemplate<String, Object> redisTemplate;
    
    // 用户ID -> Channel 的映射
    private static final ConcurrentHashMap<Long, Channel> userChannelMap = new ConcurrentHashMap<>();
    
    public WebSocketHandler(ObjectMapper objectMapper, 
                          MessageService messageService,
                          MessageProducer messageProducer,
                          RedisTemplate<String, Object> redisTemplate) {
        this.objectMapper = objectMapper;
        this.messageService = messageService;
        this.messageProducer = messageProducer;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String text = frame.text();
        ChatMessage chatMessage = objectMapper.readValue(text, ChatMessage.class);
        
        switch (chatMessage.getType()) {
            case CONNECT:
                handleConnect(ctx, chatMessage);
                break;
            case PRIVATE_MSG:
                handlePrivateMessage(chatMessage);
                break;
            case GROUP_MSG:
                handleGroupMessage(chatMessage);
                break;
            case HEARTBEAT:
                handleHeartbeat(ctx);
                break;
            default:
                log.warn("未知的消息类型: {}", chatMessage.getType());
        }
    }

    private void handleConnect(ChannelHandlerContext ctx, ChatMessage chatMessage) {
        Long userId = chatMessage.getSenderId();
        userChannelMap.put(userId, ctx.channel());
        
        // 处理离线消息
        handleOfflineMessages(userId);
        
        log.info("用户 {} 已连接", userId);
    }

    private void handlePrivateMessage(ChatMessage chatMessage) {
        // 发送消息到私聊消息队列
        messageProducer.sendPrivateMessage(chatMessage);
    }

    private void handleGroupMessage(ChatMessage chatMessage) {
        // 发送消息到群聊消息队列
        messageProducer.sendGroupMessage(chatMessage);
    }

    private void handleHeartbeat(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new TextWebSocketFrame("{\"type\":\"HEARTBEAT\"}"));
    }

    private void handleOfflineMessages(Long userId) {
        String key = "offline:message:" + userId;
        List<Object> messages = redisTemplate.opsForList().range(key, 0, -1);
        
        if (messages != null && !messages.isEmpty()) {
            Channel channel = userChannelMap.get(userId);
            messages.stream()
                .map(msg -> (ChatMessage) msg)
                .sorted((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                .forEach(message -> {
                    try {
                        channel.writeAndFlush(
                            new TextWebSocketFrame(
                                objectMapper.writeValueAsString(message)
                            )
                        );
                    } catch (Exception e) {
                        log.error("发送离线消息失败", e);
                    }
                });
            
            // 清除已发送的离线消息
            redisTemplate.delete(key);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        userChannelMap.forEach((userId, channel) -> {
            if (channel == ctx.channel()) {
                userChannelMap.remove(userId);
                log.info("用户 {} 已断开连接", userId);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("WebSocket处理异常", cause);
        ctx.close();
    }

    // 工具方法
    public boolean isUserOnline(Long userId) {
        Channel channel = userChannelMap.get(userId);
        return channel != null && channel.isActive();
    }

    public void sendMessageToUser(Long userId, TextWebSocketFrame message) {
        Channel channel = userChannelMap.get(userId);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
        }
    }
} 