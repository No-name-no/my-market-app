package org.mnuykin.mymarket.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.mnuykin.mymarket.entity.CartItem;
import org.mnuykin.mymarket.repository.dto.CartItemData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartRepository extends ReactiveCrudRepository<CartItem, Long> {

    @Query("SELECT SUM(i.price * ci.count) FROM cart_items ci JOIN items i ON ci.item_id = i.id WHERE ci.user_id = :userId")
    Mono<Long> getCartTotal(@Param("userId") Long userId);

    Mono<CartItem> getCartItemByItemIdAndUserId(Long itemId, Long userId);
    Mono<CartItem> findByItemId(Long itemId);

    @Query("""
            Select\s
                cartItem.id as id, item.id as item, cartItem.count as count, item.price as price
            From cart_items cartItem
                Inner join items item on cartItem.item_id = item.id
            WHERE cartItem.user_id = :userId
           \s""")
    Flux<CartItemData> findCartItemDataAll(@Param("userId") Long userId);

    Flux<CartItem> findAllByUserId(Long userId);
}