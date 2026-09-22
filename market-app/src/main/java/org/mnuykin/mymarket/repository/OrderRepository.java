package org.mnuykin.mymarket.repository;

import org.mnuykin.mymarket.entity.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Mono<Order> getOrderByIdAndUserId(Long id, Long userId);
    Flux<Order> findAllByUserId(Long userId);
}