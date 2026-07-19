package org.mnuykin.mymarket.repository;

import org.mnuykin.mymarket.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    //@Query("Select sum(item.price * cartItem.count) From CartItem cartItem Inner join Item item on item.id = cartItem.item_id")
    @Query("SELECT SUM(item.price * cartItem.count) FROM CartItem cartItem JOIN cartItem.item item")
    Long getCartTotal();

    Optional<CartItem> getCartItemByItem_Id(Long id);
}