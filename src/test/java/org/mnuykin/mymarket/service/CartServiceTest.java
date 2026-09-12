package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest extends BaseServiceTest {
    @Autowired
    private CartServiceImpl cartService;

    @Test
    void executeGetTotalEmpty(){
        assertEquals(0L, cartService.getTotal());
    }

    @Test
    void executeActionWithNotFoundItem(){
        assertThrows(NotFoundException.class, () -> cartService.executeAction(-100L, ItemAction.MINUS));
    }

    @Test
    void minusAndDeleteExecuteActionWithEmptyCart(){
        List<ItemDto> itemDtoList = cartService.getItems();
        assertTrue(itemDtoList.isEmpty());

        cartService.executeAction(id, ItemAction.MINUS);
        itemDtoList = cartService.getItems();
        assertTrue(itemDtoList.isEmpty());

        cartService.executeAction(id, ItemAction.DELETE);
        itemDtoList = cartService.getItems();
        assertTrue(itemDtoList.isEmpty());
    }

    @Test
    void executeAction(){
        long total = cartService.getTotal();
        assertEquals(0L, total);

        List<ItemDto> itemDtoList = cartService.getItems();
        assertTrue(itemDtoList.isEmpty());

        cartService.executeAction(id, ItemAction.PLUS);
        itemDtoList = cartService.getItems();
        assertFalse(itemDtoList.isEmpty());
        assertEquals(1, itemDtoList.size());
        ItemDto itemDto = itemDtoList.getFirst();
        assertNotNull(itemDto);
        assertEquals(id, itemDto.getId());
        assertEquals(title, itemDto.getTitle());
        assertEquals(img_path, itemDto.getImgPath());
        assertEquals(description, itemDto.getDescription());
        assertEquals(1, itemDto.getCount());
        assertEquals(price, itemDto.getPrice());

        cartService.executeAction(id, ItemAction.PLUS);
        total = cartService.getTotal();
        assertEquals(price*2, total);

        cartService.executeAction(id, ItemAction.DELETE);
        itemDtoList = cartService.getItems();
        assertTrue(itemDtoList.isEmpty());
    }
}