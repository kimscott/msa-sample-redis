package com.example.redisTemplate.pubsub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class RedisMessageSubscriber  implements MessageListener {

    public static List<String> messageList = new ArrayList<String>();

    /**
     * pattern 을 통해 채널 매칭을 위한 패턴을 정의할수 있음
     * 패턴을 정의하면 여러 채널로 부터 구독이 가능함
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        messageList.add(message.toString());
        System.out.println("Message received: " + message.toString());

    }
}
