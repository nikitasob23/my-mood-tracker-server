package com.niksob.database_service.config.cache;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class RedisCacheCleaner implements ApplicationListener<ContextRefreshedEvent> {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheCleaner(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
