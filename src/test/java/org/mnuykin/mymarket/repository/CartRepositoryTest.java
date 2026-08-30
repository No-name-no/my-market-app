package org.mnuykin.mymarket.repository;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.entity.CartItem;
import org.mnuykin.mymarket.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void getCartTotalTestEmpty(){
        assertNull(cartRepository.getCartTotal());
    }

    @Test
    void getCartTotalTest(){
        Item item = new Item();
        item.setTitle("Наушники");
        item.setDescription("Беспроводные наушники");
        item.setPrice(10000L);
        Item item2 = new Item();
        item2.setTitle("Наушники");
        item2.setDescription("Sony наушники");
        item2.setPrice(1000L);

        List<Item> items = itemRepository.saveAll(List.of(item, item2));

        CartItem cartItem = new CartItem();
        cartItem.setItem(items.getFirst());
        cartItem.setCount(2);
        cartRepository.save(cartItem);
        Long assertTotal = items.getFirst().getPrice()*2;
        Long total = cartRepository.getCartTotal();
        assertEquals(assertTotal, total);

        CartItem cartItem2 = new CartItem();
        cartItem2.setItem(items.getLast());
        cartItem2.setCount(1);
        cartRepository.save(cartItem2);
        assertTotal += items.getLast().getPrice();
        total = cartRepository.getCartTotal();
        assertEquals(assertTotal, total);
    }
}
