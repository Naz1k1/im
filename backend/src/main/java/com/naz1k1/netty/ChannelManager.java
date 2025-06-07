package com.naz1k1.netty;

import io.netty.channel.group.ChannelGroup;

import java.nio.channels.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelManager {
    private static final Map<Long, Channel> userChannelMap = new ConcurrentHashMap<>();
    private static final Map<Long, ChannelGroup> groupChannelMap = new ConcurrentHashMap<>();

    public static void addUserChannel(Long userId, Channel channel) {
        userChannelMap.put(userId,channel);
    }


}
