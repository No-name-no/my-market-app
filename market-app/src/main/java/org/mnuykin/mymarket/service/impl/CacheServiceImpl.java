package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.config.CacheConfig;
import org.mnuykin.mymarket.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CacheServiceImpl implements CacheService {
    final private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Autowired
    public CacheServiceImpl(ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<Object> get(String key) {
        return reactiveRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Mono<Boolean> save(String key, Object object) {
        return reactiveRedisTemplate.opsForValue().set(key, object, CacheConfig.CACHE_TTL);
    }

    @Override
    public Mono<Boolean> save(String key, List<?> object) {
        return reactiveRedisTemplate.opsForList()
                .rightPushAll(key, object.toArray())
                .then(reactiveRedisTemplate.expire(key, CacheConfig.CACHE_TTL));
    }

    @Override
    public Flux<Object> getObjs(String key) {
        return reactiveRedisTemplate.opsForList().range(key, 0, -1);
    }
}