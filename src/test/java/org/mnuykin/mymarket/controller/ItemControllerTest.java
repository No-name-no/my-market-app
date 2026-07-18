package org.mnuykin.mymarket.controller;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.service.CartService;
import org.mnuykin.mymarket.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private CartService cartService;

    @Test
    void getItems_shouldReturnItemsViewWithPagingAndTransformedItems() throws Exception {
        List<ItemDto> items = List.of(
                new ItemDto(1L, "Product1", "Desc1", "img1.jpg", 100L, 2),
                new ItemDto(2L, "Product2", "Desc2", "img2.jpg", 200L, 1),
                new ItemDto(3L, "Product3", "Desc3", "img3.jpg", 300L, 5),
                new ItemDto(4L, "Product4", "Desc4", "img4.jpg", 400L, 3)
        );
        Page<ItemDto> page = new PageImpl<>(items);
        when(itemService.findItems(null, ItemsSort.NO, 1, 5)).thenReturn(page);

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("items"))
                .andExpect(model().attributeExists("items", "sort", "paging"))
                .andExpect(model().attribute("sort", ItemsSort.NO));
        verify(itemService, times(1)).findItems(null, ItemsSort.NO, 1, 5);
        verifyNoMoreInteractions(itemService);
        verifyNoInteractions(cartService);
    }

    @Test
    void getItems_withSearchAndPaging_shouldReturnFilteredItems() throws Exception {
        String search = "Product";
        ItemsSort sort = ItemsSort.PRICE;
        int pageNumber = 2;
        int pageSize = 10;
        List<ItemDto> items = List.of(
                new ItemDto(10L, "Product10", "Desc10", "img10.jpg", 500L, 1)
        );
        Page<ItemDto> page = new PageImpl<>(items);
        when(itemService.findItems(search, sort, pageNumber, pageSize)).thenReturn(page);

        mockMvc.perform(get("/items")
                        .param("search", search)
                        .param("sort", sort.name())
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(view().name("items"))
                .andExpect(model().attribute("search", search))
                .andExpect(model().attribute("sort", sort));

        verify(itemService, times(1)).findItems(search, sort, pageNumber, pageSize);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    void postItems_shouldExecuteActionAndRedirectWithAllParameters() throws Exception {
        Long id = 1L;
        String search = "test";
        ItemsSort sort = ItemsSort.PRICE;
        int pageNumber = 3;
        int pageSize = 7;
        ItemAction action = ItemAction.PLUS;

        mockMvc.perform(post("/items")
                        .param("id", id.toString())
                        .param("search", search)
                        .param("sort", sort.name())
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("action", action.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items?search=" + search +
                        "&sort=" + sort.name() +
                        "&pageNumber=" + pageNumber +
                        "&pageSize=" + pageSize));

        verify(cartService, times(1)).executeAction(id, action);
        verifyNoMoreInteractions(cartService);
        verifyNoInteractions(itemService);
    }

    @Test
    void getItemById_shouldReturnItemView() throws Exception {
        Long id = 5L;
        ItemDto item = new ItemDto(id, "Special", "Desc", "img.jpg", 999L, 1);
        when(itemService.getItemById(id)).thenReturn(item);

        mockMvc.perform(get("/items/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attribute("item", item));

        verify(itemService, times(1)).getItemById(id);
        verifyNoMoreInteractions(itemService);
        verifyNoInteractions(cartService);
    }

    @Test
    void postItemById_shouldExecuteActionAndReturnItemViewWithUpdatedItem() throws Exception {
        Long id = 7L;
        ItemAction action = ItemAction.DELETE;
        ItemDto updatedItem = new ItemDto(id, "Updated", "NewDesc", "new.jpg", 100L, 0);
        when(itemService.getItemById(id)).thenReturn(updatedItem);

        mockMvc.perform(post("/items/{id}", id)
                        .param("action", action.name()))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attribute("item", updatedItem));

        verify(cartService, times(1)).executeAction(id, action);
        verify(itemService, times(1)).getItemById(id);
        verifyNoMoreInteractions(cartService, itemService);
    }
}