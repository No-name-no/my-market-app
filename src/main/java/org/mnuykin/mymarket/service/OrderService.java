package org.mnuykin.mymarket.service;

import org.mnuykin.mymarket.model.OrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Flux<OrderDto> getOrder();
    Mono<OrderDto> getOrderById(Long id);
    Mono<OrderDto> create();
}