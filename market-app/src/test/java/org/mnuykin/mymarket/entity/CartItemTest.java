package org.mnuykin.mymarket.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartItemTest {
    private CartItem cartItem;

    @BeforeEach
    void prepareCartItem(){
        cartItem = new CartItem(1L, 1L, Integer.MAX_VALUE);
    }

    @Test
    void addItem(){
        cartItem.addItem();
        assertEquals(Integer.MAX_VALUE, cartItem.getCount());
    }

    @Test
    void deleteItem(){
        cartItem.deleteItem();
        assertEquals(Integer.MAX_VALUE-1, cartItem.getCount());
        cartItem.setCount(0);
        cartItem.deleteItem();
        assertEquals(0, cartItem.getCount());
    }
}
