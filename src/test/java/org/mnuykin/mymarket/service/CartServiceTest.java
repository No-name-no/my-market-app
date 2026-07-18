package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.mapper.ItemMapper;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.service.impl.CartServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void executeAction_plus() {
        Long id = 1L;
        Item item = mock(Item.class);
        when(itemRepository.getItemById(id)).thenReturn(Optional.of(item));

        cartService.executeAction(id, ItemAction.PLUS);

        verify(item).addItem();
        verify(itemRepository).save(item);
        verifyNoMoreInteractions(itemRepository, item);
    }

    @Test
    void executeAction_minus() {
        Long id = 2L;
        Item item = mock(Item.class);
        when(itemRepository.getItemById(id)).thenReturn(Optional.of(item));

        cartService.executeAction(id, ItemAction.MINUS);

        verify(item).deleteItem();
        verify(itemRepository).save(item);
        verifyNoMoreInteractions(itemRepository, item);
    }

    @Test
    void executeAction_delete() {
        Long id = 3L;
        Item item = mock(Item.class);
        when(itemRepository.getItemById(id)).thenReturn(Optional.of(item));

        cartService.executeAction(id, ItemAction.DELETE);

        verify(item).setCount(0);
        verify(itemRepository).save(item);
        verifyNoMoreInteractions(itemRepository, item);
    }

    @Test
    void executeAction_itemNotFound_throws() {
        Long id = 4L;
        when(itemRepository.getItemById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.executeAction(id, ItemAction.PLUS));

        verify(itemRepository).getItemById(id);
        verifyNoMoreInteractions(itemRepository);
        verifyNoInteractions(itemMapper);
    }

    @Test
    void getItems_shouldReturnDtoList() {
        List<Item> items = List.of(new Item(), new Item());
        List<ItemDto> expectedDtos = List.of(new ItemDto(), new ItemDto());
        when(itemRepository.findByCountGreaterThan(0)).thenReturn(items);
        when(itemMapper.toDtoList(items)).thenReturn(expectedDtos);

        List<ItemDto> result = cartService.getItems();

        assertEquals(expectedDtos, result);
        verify(itemRepository).findByCountGreaterThan(0);
        verify(itemMapper).toDtoList(items);
        verifyNoMoreInteractions(itemRepository, itemMapper);
    }

    @Test
    void getTotal_shouldReturnTotalFromRepository() {
        Long total = 100L;
        when(itemRepository.getCartTotal()).thenReturn(total);

        long result = cartService.getTotal();

        assertEquals(total, result);
        verify(itemRepository).getCartTotal();
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getTotal_shouldReturnZeroWhenRepositoryReturnsNull() {
        when(itemRepository.getCartTotal()).thenReturn(null);

        long result = cartService.getTotal();

        assertEquals(0L, result);
        verify(itemRepository).getCartTotal();
        verifyNoMoreInteractions(itemRepository);
    }
}