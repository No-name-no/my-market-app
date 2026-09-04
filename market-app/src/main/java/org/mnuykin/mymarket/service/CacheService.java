package org.mnuykin.mymarket.service;

import org.mnuykin.mymarket.model.ItemDto;
import reactor.core.publisher.Flux;

public interface CacheService {
    Flux<Object> get(String key);

}
