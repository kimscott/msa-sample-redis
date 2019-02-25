package com.example.redisTemplate.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

public class RedisMessagePublisher {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ChannelTopic topic;

    public RedisMessagePublisher(RedisTemplate redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    /**
     * convertAndSend() 메서드는 목적지( 채널 )에 메시지를 전달하는 역할을 합니다.
     * pub sub 모델의 특징 상 채널을 구독하고 있는 모든 구독자에게 메시지가 전달됩니다.
     */
    public void publish(String message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

}
