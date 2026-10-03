package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class CartServiceTest extends BaseServiceTest {
    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private CacheService cacheService;

    @Test
    @WithMockUser(username = "test", password = "{bcrypt}$2a$10$jJt7W4VW0300oj.Lqon17uQpg7jL7qxjRQZvd/5VjR9fwFBzc5dI6")
    void test(){
        doReturn(Mono.just(true)).when(cacheService).save(anyString(), anyList());
        doReturn(Mono.just(true)).when(cacheService).save(anyString(), any(Object.class));
        when(cacheService.get(any())).thenReturn(Mono.empty());

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