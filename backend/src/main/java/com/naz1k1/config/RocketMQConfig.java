package com.naz1k1.config;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * RocketMQ配置类
 */
@Configuration
public class RocketMQConfig {

    /**
     * 消息主题常量
     */
    public static final String MESSAGE_TOPIC = "im-message-topic";
    public static final String MESSAGE_PERSISTENCE_TOPIC = "im-message-persistence-topic";
    public static final String MESSAGE_COUNTER_TOPIC = "im-message-counter-topic";
    public static final String MESSAGE_PUSH_TOPIC = "im-message-push-topic";

    /**
     * 消息生产者组
     */
    public static final String PRODUCER_GROUP = "im-producer-group";

    /**
     * 消息消费者组
     */
    public static final String CONSUMER_GROUP_PERSISTENCE = "im-consumer-persistence-group";
    public static final String CONSUMER_GROUP_COUNTER = "im-consumer-counter-group";
    public static final String CONSUMER_GROUP_PUSH = "im-consumer-push-group";
} 