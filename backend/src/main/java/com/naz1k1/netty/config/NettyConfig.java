package com.naz1k1.netty.config;

import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfig {
    
    @Value("${netty.websocket.port:8888}")
    private int websocketPort;
    
    @Value("${netty.websocket.path:/ws}")
    private String websocketPath;
    
    @Value("${netty.boss.thread.count:1}")
    private int bossCount;
    
    @Value("${netty.worker.thread.count:4}")
    private int workerCount;

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    @Bean(name = "httpServerCodec")
    public HttpServerCodec httpServerCodec() {
        return new HttpServerCodec();
    }

    @Bean(name = "httpObjectAggregator")
    public HttpObjectAggregator httpObjectAggregator() {
        return new HttpObjectAggregator(65536);
    }

    @Bean(name = "chunkedWriteHandler")
    public ChunkedWriteHandler chunkedWriteHandler() {
        return new ChunkedWriteHandler();
    }

    @Bean(name = "webSocketServerProtocolHandler")
    public WebSocketServerProtocolHandler webSocketServerProtocolHandler() {
        return new WebSocketServerProtocolHandler(websocketPath, null, true);
    }
} 