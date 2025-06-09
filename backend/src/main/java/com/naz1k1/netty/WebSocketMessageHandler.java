package com.naz1k1.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naz1k1.model.dto.MessageDTO;
import com.naz1k1.service.MessageService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Sharable  // 标记为可共享的Handler
public class WebSocketMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final ConcurrentHashMap<Long, Channel> userChannelMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final MessageService messageService;

    @Autowired
    public WebSocketMessageHandler(MessageService messageService) {
        this.messageService = messageService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String text = frame.text();
        log.debug("收到WebSocket消息: {}", text);

        try {
            MessageDTO messageDTO = objectMapper.readValue(text, MessageDTO.class);

            // 处理消息
            if (messageDTO.getMessageType() == 1) {
                // 私聊消息
                handlePrivateMessage(messageDTO);
            } else if (messageDTO.getMessageType() == 2) {
                // 群聊消息
                handleGroupMessage(messageDTO);
            }
        } catch (Exception e) {
            log.error("处理WebSocket消息异常:", e);
            // 可以选择向客户端发送错误消息
            ctx.channel().writeAndFlush(new TextWebSocketFrame("消息处理失败: " + e.getMessage()));
        }
    }

    private void handlePrivateMessage(MessageDTO messageDTO) throws Exception {
        // 保存消息到MongoDB
        messageService.sendPrivateMessage(MessageConverter.toPrivateMessage(messageDTO));

        // 发送消息给接收者
        Channel receiverChannel = userChannelMap.get(messageDTO.getReceiverId());
        if (receiverChannel != null && receiverChannel.isActive()) {
            receiverChannel.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(messageDTO)));
        }
    }

    private void handleGroupMessage(MessageDTO messageDTO) throws Exception {
        // 保存消息到MongoDB
        messageService.sendGroupMessage(MessageConverter.toGroupMessage(messageDTO));

        // TODO: 获取群成员列表并发送消息
        // 这里需要调用群组服务获取群成员列表，然后遍历发送
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("客户端连接: {}", ctx.channel().id());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("客户端断开: {}", ctx.channel().id());
        // 清理用户Channel映射
        userChannelMap.forEach((userId, channel) -> {
            if (channel == ctx.channel()) {
                userChannelMap.remove(userId);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("WebSocket异常:", cause);
        ctx.close();
    }

    // 用户认证后调用此方法绑定用户ID和Channel
    public void bindUser(Long userId, Channel channel) {
        userChannelMap.put(userId, channel);
        log.info("用户 {} 绑定到Channel {}", userId, channel.id());
    }
}