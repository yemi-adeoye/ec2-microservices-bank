package com.yemiadeoye.banks_ms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration()
public class DataConfiguration {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        var standAloneConfig = new RedisStandaloneConfiguration("localhost", 6379);
        standAloneConfig.setPassword("13c21091b1b4d67dbbb7835124596b15");
        return new LettuceConnectionFactory(standAloneConfig);
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        var redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

}
