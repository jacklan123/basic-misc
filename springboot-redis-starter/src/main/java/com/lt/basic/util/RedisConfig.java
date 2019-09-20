package com.lt.basic.util;

import com.lt.basic.util.redis.KryoRedisSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;

/**
 * @author lantian
 * @date 2019/09/20
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisKryoTemplate {



    @Bean
    public RedisTemplate<Object, Object> redisKryoTemplate(RedisConnectionFactory redisConnectionFactory) throws
        UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new KryoRedisSerializer(Object.class));
        template.setValueSerializer(new KryoRedisSerializer(Object.class));
        template.setHashKeySerializer(new KryoRedisSerializer(Object.class));
        template.setHashValueSerializer(new KryoRedisSerializer(Object.class));
        return template;
    }





}
