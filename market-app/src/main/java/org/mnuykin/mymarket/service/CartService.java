package org.mnuykin.mymarket.service;

import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemAction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartService {
    Mono<Void> executeAction(Long id, ItemAction action);
    Flux<ItemDto> getItems();
    Mono<Long> getTotal();
}