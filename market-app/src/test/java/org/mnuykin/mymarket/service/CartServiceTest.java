package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

class CartServiceTest extends BaseServiceTest {
    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private CacheService cacheService;

    @Test
    void test(){
        doReturn(Mono.just(true)).when(cacheService).save(anyString(), anyList());
        doReturn(Mono.just(true)).when(cacheService).save(anyString(), any(Object.class));

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