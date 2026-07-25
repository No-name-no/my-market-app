package org.mnuykin.mymarket.repository;

import org.mnuykin.mymarket.entity.CartItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CartRepository extends ReactiveCrudRepository<CartItem, Long> {

    @Query("SELECT SUM(item.price * cartItem.count) FROM CartItem cartItem JOIN cartItem.item item")
    Mono<Long> getCartTotal();

    Mono<CartItem> getCartItemByItem_Id(Long id);
}