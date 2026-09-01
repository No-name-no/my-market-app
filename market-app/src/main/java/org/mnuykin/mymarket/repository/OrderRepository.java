package org.mnuykin.mymarket.repository;

import org.mnuykin.mymarket.entity.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Mono<Order> getOrderById(Long id);
}