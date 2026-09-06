package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.model.PageItemDto;
import org.mnuykin.mymarket.service.impl.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class ItemServiceTest extends BaseServiceTest{

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private CacheService cacheService;

    @Test
    void findItems_withSearch_shouldReturnFilteredItems(){
        when(cacheService.get(any())).thenReturn(Mono.empty());
        doReturn(Mono.just(true)).when(cacheService).save(anyString(), anyList());
        doReturn(Mono.just(true)).when(cacheService).save(anyString(), any(Object.class));
        doReturn(Flux.empty()).when(cacheService).getObjs(any());

        PageItemDto itemDtos = itemService.findItems(description, ItemsSort.NO, 0, 2).block();
        assertNotNull(itemDtos);
        assertEquals(5, itemDtos.getTotal());
        assertEquals(2, itemDtos.getContent().size());
        assertEquals(description, itemDtos.getContent().getFirst().getDescription());
    }

    @Test
    void findItems_withSearchByTitle_shouldReturnFilteredItems(){
        when(cacheService.get(any())).thenReturn(Mono.empty());
        doReturn(Mono.just(true)).when(cacheService).save(anyString(), anyList());
        doReturn(Mono.just(true)).when(cacheService).save(anyString(), any(Object.class));
        doReturn(Flux.empty()).when(cacheService).getObjs(any());

        PageItemDto itemDtos = itemService.findItems(title, ItemsSort.NO, 0, 2).block();
        assertNotNull(itemDtos);
        assertEquals(1, itemDtos.getTotal());
        assertEquals(1, itemDtos.getContent().size());
        assertEquals(description, itemDtos.getContent().getFirst().getDescription());
    }

    @Test
    void getItemById_shouldReturnItemDto(){
        when(cacheService.get(any())).thenReturn(Mono.empty());
        doReturn(Mono.just(true)).when(cacheService).save(anyString(), anyList());
        doReturn(Mono.just(true)).when(cacheService).save(anyString(), any(Object.class));
        doReturn(Flux.empty()).when(cacheService).getObjs(any());

        ItemDto itemDto = itemService.getItemById(id).block();

        assertNotNull(itemDto);
        assertEquals(id, itemDto.getId());
        assertEquals(title, itemDto.getTitle());
        assertEquals(img_path, itemDto.getImgPath());
        assertEquals(description, itemDto.getDescription());
        assertEquals(0, itemDto.getCount());
        assertEquals(price, itemDto.getPrice());
    }

    @Test
    void getItemById_shouldThrowException(){
        when(cacheService.get(any())).thenReturn(Mono.empty());
        assertThrows(NotFoundException.class, () -> itemService.getItemById(-1L).block());
    }
}
