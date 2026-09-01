package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.service.impl.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;

public class ItemServiceTest extends BaseServiceTest{

    @Autowired
    private ItemServiceImpl itemService;

    @Test
    void findItems_withSearch_shouldReturnFilteredItems(){
        Page<ItemDto> itemDtos = itemService.findItems(description, ItemsSort.NO, 0, 2).block();
        assertNotNull(itemDtos);
        assertEquals(5, itemDtos.getTotalElements());
        assertEquals(3, itemDtos.getTotalPages());
        assertEquals(2, itemDtos.getContent().size());
        assertEquals(description, itemDtos.getContent().getFirst().getDescription());
    }

    @Test
    void findItems_withSearchByTitle_shouldReturnFilteredItems(){
        Page<ItemDto> itemDtos = itemService.findItems(title, ItemsSort.NO, 0, 2).block();
        assertNotNull(itemDtos);
        assertEquals(1, itemDtos.getTotalElements());
        assertEquals(1, itemDtos.getTotalPages());
        assertEquals(1, itemDtos.getContent().size());
        assertEquals(description, itemDtos.getContent().getFirst().getDescription());
    }

    @Test
    void getItemById_shouldReturnItemDto(){
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
        assertThrows(NotFoundException.class, () -> itemService.getItemById(-1L).block());
    }
}
