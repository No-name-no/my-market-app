package org.mnuykin.mymarket.controller;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest extends BaseControllerTest {
    @Test
    void getCart_shouldReturnCartViewWithItemsAndTotal() throws Exception {
        List<ItemDto> items = List.of(
                new ItemDto(1L, "Product A", "Desc A", "img1.jpg", 100L, 2),
                new ItemDto(2L, "Product B", "Desc B", "img2.jpg", 200L, 1)
        );
        Long total = 99L;
        when(cartService.getItems()).thenReturn(items);
        when(cartService.getTotal()).thenReturn(total);

        mockMvc.perform(get("/cart/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("items", items))
                .andExpect(model().attribute("total", total));

        verify(cartService, times(1)).getItems();
        verify(cartService, times(1)).getTotal();
        verifyNoMoreInteractions(cartService);
    }

    @Test
    void postCart_shouldExecuteActionAndReturnCartView() throws Exception {
        // given
        Long id = 1L;
        ItemAction action = ItemAction.PLUS;
        List<ItemDto> items = List.of(
                new ItemDto(1L, "Product A", "Desc A", "img1.jpg", 100L, 2),
                new ItemDto(2L, "Product B", "Desc B", "img2.jpg", 200L, 1)
        );
        Long total = 99L;

        // mock the service calls after action execution
        when(cartService.getItems()).thenReturn(items);
        when(cartService.getTotal()).thenReturn(total);

        // when & then
        mockMvc.perform(post("/cart/items")
                        .param("id", id.toString())
                        .param("action", action.name()))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("items", items))
                .andExpect(model().attribute("total", total));

        verify(cartService, times(1)).executeAction(id, action);
        verify(cartService, times(1)).getItems();
        verify(cartService, times(1)).getTotal();
        verifyNoMoreInteractions(cartService);
    }
}