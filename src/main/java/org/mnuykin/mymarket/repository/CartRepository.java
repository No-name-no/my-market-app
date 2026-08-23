package org.mnuykin.mymarket.repository;

import org.mnuykin.mymarket.entity.CartItem;
import org.mnuykin.mymarket.repository.dto.CartItemData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartRepository extends ReactiveCrudRepository<CartItem, Long> {

    @Query("SELECT SUM(item.price * cartItem.count) FROM CartItem cartItem JOIN cartItem.item item")
    Mono<Long> getCartTotal();

    Mono<CartItem> getCartItemByItemId(Long id);
    Mono<CartItem> findByItemId(Long itemId);

    //Long id, Long itemId, Integer count, Long price
    @Query("""
            Select\s
                cartItem.id as is, cartItem.item_id as itemId, cartItem.count as count, item.price as price
            From order_items cartItem
                Inner join items item on cartItem.item_id = item.id
           \s""")
    Flux<CartItemData> findCartItemDataAll();

}