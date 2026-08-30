package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.advice.exception.CartEmptyException;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest extends BaseServiceTest{

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Test
    void buyWithCardEmpty(){
        assertThrows(CartEmptyException.class, () -> orderService.create());
    }

    @Test
    void buy(){
        cartService.executeAction(id, ItemAction.PLUS);
        cartService.executeAction(id, ItemAction.PLUS);
        OrderDto orderDto = orderService.create();

        assertNotNull(orderDto);
        assertEquals(price*2,orderDto.getTotalSum());
        assertFalse(orderDto.getItems().isEmpty());
        assertEquals(1, orderDto.getItems().size());
        ItemDto itemDto = orderDto.getItems().getFirst();
        assertNotNull(itemDto);
        assertEquals(title, itemDto.getTitle());
        assertEquals(2, itemDto.getCount());
        assertEquals(price, itemDto.getPrice());

        List<OrderDto> orderDtos = orderService.getOrder();
        assertNotNull(orderDtos);
        assertFalse(orderDtos.isEmpty());
        OrderDto getOrder = orderDtos.getFirst();
        assertEquals(getOrder, orderDto);

        List<ItemDto> itemDtoList = cartService.getItems();
        assertTrue(itemDtoList.isEmpty());
    }
}