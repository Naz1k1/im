package com.naz1k1.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Slf4j
@Component
public class NettyServer {

    @Value("${netty.port:8090}")
    private int port;

    @Value("${netty.host:0.0.0.0}")
    private String host;

    private final WebSocketMessageHandler webSocketMessageHandler;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;

    // 注入WebSocketMessageHandler
    public NettyServer(WebSocketMessageHandler webSocketMessageHandler) {
        this.webSocketMessageHandler = webSocketMessageHandler;
    }

    @PostConstruct
    public void start() {
        // 在新线程中启动Netty服务器
        new Thread(() -> {
            try {
                bossGroup = new NioEventLoopGroup(1);
                workerGroup = new NioEventLoopGroup();

                ServerBootstrap bootstrap = new ServerBootstrap()
                        .group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(new InetSocketAddress(host, port))
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ch.pipeline()
                                        .addLast(new HttpServerCodec())
                                        .addLast(new ChunkedWriteHandler())
                                        .addLast(new HttpObjectAggregator(65536))
                                        .addLast(new WebSocketServerProtocolHandler("/ws"))
                                        .addLast(webSocketMessageHandler); // 使用注入的handler
                            }
                        });

                channelFuture = bootstrap.bind().sync();
                log.info("Netty WebSocket server started on {}:{}", host, port);
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                log.error("Netty server start error:", e);
            }
        }, "netty-server-thread").start();
    }

    @PreDestroy
    public void destroy() {
        log.info("Shutting down Netty server...");
        if (channelFuture != null) {
            channelFuture.channel().close().syncUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        log.info("Netty server shutdown complete.");
    }
}