package com.example.redisTemplate;

import com.example.redisTemplate.pubsub.RedisMessagePublisher;
import com.example.redisTemplate.pubsub.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Autowired
    private Environment environment;

//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
//        redisConf.setHostName(environment.getProperty("spring.redis.host"));
//        redisConf.setPort(Integer.parseInt(environment.getProperty("spring.redis.port")));
////        redisConf.setPassword(RedisPassword.of(environment.getProperty("spring.redis.password")));
//
//        return new LettuceConnectionFactory(redisConf);
//    }


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
        redisConf.setHostName(environment.getProperty("spring.redis.host"));
        redisConf.setPort(Integer.parseInt(environment.getProperty("spring.redis.port")));
        redisConf.setPassword(RedisPassword.of(environment.getProperty("spring.redis.password")));

        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisConf);

        return connectionFactory;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))  // 케쉬데이터 저장 시간
                .disableCachingNullValues();        // 케쉬는 null 을 허용 안함
        return cacheConfig;
    }
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager rcm = RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfiguration())
                .transactionAware()
                .build();
        return rcm;
    }

    /**
     * redisTemplate 을 사용하에 redis 데이터를 직접 컨트롤을 할 수가 있다.
     * redis pub/sub 을 사용할적에 메세지를 보내기 위하느 Template 으로 쓰인다.
     */
    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate<>();

        // Serializer 를 등록하지 않으면 Default Serializer로 JdkSerializationRedisSerializer 를 사용하며
        // 이때 모든 값은 UniCode로 되어버린다
        // 출처: https://yonguri.tistory.com/entry/스프링부트-SpringBoot-개발환경-구성-3-Redis-설정 [Yorath's 블로그]
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

    /**
     * 메시지 큐를 위한 Bean을 추가
     */
    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new RedisMessageSubscriber());
    }

    /**
     * 채널의 메세지를 주고받을수있는 컨테이너 정의
     * 컨테이너는 redis 로 부터 메세지를 받고, 구독자에게 메세지를 보내는 역할
     */
    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListener(), topic());
        return container;
    }

    /**
     * 메세지를 게시하는 bean
     * RedisMessagePublisher 를 Autowired 하여 사용할수 있도록 선
     */
    @Bean
    RedisMessagePublisher redisPublisher() {
        return new RedisMessagePublisher(redisTemplate(), topic());
    }


    /**
     * 메세지를 주고 받을수있는 공간인 Channel 등록
     */
    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("messageQueue");
    }


}
