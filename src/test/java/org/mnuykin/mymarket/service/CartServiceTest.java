package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CartServiceTest extends BaseServiceTest {
    @Autowired
    private CartServiceImpl cartService;

    @Test
    void test(){
        assertEquals(0L, cartService.getTotal().block());

        List<ItemDto> itemDtoList = cartService.getItems().collectList().block();
        assertNotNull(itemDtoList);
        assertTrue(itemDtoList.isEmpty());

        cartService.executeAction(id, ItemAction.PLUS).block();
        itemDtoList = cartService.getItems().collectList().block();
        assertNotNull(itemDtoList);
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

        cartService.executeAction(id, ItemAction.PLUS).block();
        assertEquals(price*2, cartService.getTotal().block());

        cartService.executeAction(id, ItemAction.DELETE).block();
        itemDtoList = cartService.getItems().collectList().block();
        assertNotNull(itemDtoList);
        assertTrue(itemDtoList.isEmpty());
    }
}