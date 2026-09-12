package org.mnuykin.mymarket.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CacheService {
    Mono<Object> get(String key);
    Mono<Boolean> save(String key, Object object);
    Mono<Boolean> save(String key, List<?> object);
    Flux<Object> getObjs(String key);
}
