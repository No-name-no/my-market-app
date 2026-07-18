package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.mapper.ItemMapperImpl;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.service.impl.ItemServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapperImpl itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void findItems_withSearch_shouldReturnFilteredItems() {
        String search = "Product A";
        Item item = new Item(); item.setId(1L); item.setTitle(search); item.setDescription("Description A");
        Page<Item> page = new PageImpl<>(List.of(item));

        when(itemRepository.findByDescriptionLikeOrTitleLike(
                search, search, PageRequest.of(1, 5, Sort.unsorted()))).thenReturn(page);
        when(itemMapper.toDto(item)).thenReturn(new ItemDto(1L, search, "Description A", "", 100L, 0));

        Page<ItemDto> result = itemService.findItems(search, ItemsSort.NO, 1, 5);

        assertEquals(1, result.getContent().size());
        assertEquals(search, result.getContent().getFirst().getTitle());

        verify(itemRepository, times(1))
                .findByDescriptionLikeOrTitleLike(search, search, PageRequest.of(1, 5, Sort.unsorted()));
    }

    @Test
    void getItemById_shouldReturnItemDto(){
        final Long id = 1L;
        Item item = new Item();
        item.setId(id);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(id);

        when(itemRepository.getItemById(id)).thenReturn(Optional.of(item));
        when(itemMapper.toDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.getItemById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(itemRepository, times(1)).getItemById(1L);
        verify(itemMapper, times(1)).toDto(item);
    }

    @Test
    void getItemById_shouldThrowException(){
        when(itemRepository.getItemById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> itemService.getItemById(1L));

        verify(itemRepository, times(1)).getItemById(1L);
        verifyNoInteractions(itemMapper);
    }
}
