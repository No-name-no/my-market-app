package org.mnuykin.mymarket.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.model.PageItemDto;
import org.mnuykin.mymarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class ItemControllerTest extends BaseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CartService cartService;

    @Test
    void getItems_shouldReturnItemsViewWithPagingAndTransformedItems() {
        List<ItemDto> items = List.of(
                new ItemDto(1L, "Product1", "Desc1", "img1.jpg", 100L, 2),
                new ItemDto(2L, "Product2", "Desc2", "img2.jpg", 200L, 1),
                new ItemDto(3L, "Product3", "Desc3", "img3.jpg", 300L, 5),
                new ItemDto(4L, "Product4", "Desc4", "img4.jpg", 400L, 3)
        );
        PageItemDto page = new PageItemDto();
        page.setContent(items);
        when(itemService.findItems(null, ItemsSort.NO, 0, 5)).thenReturn(Mono.just(page));

        webTestClient.get().uri("/items")
                .exchange().expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class).value(
                        body -> Assertions.assertThat(body)
                        .isNotEmpty()
                        .contains("items")
                );

        verify(itemService, times(1)).findItems(null, ItemsSort.NO, 0, 5);
        verifyNoMoreInteractions(itemService);
        verifyNoInteractions(cartService);
    }

    @Test
    void getItems_withSearchAndPaging_shouldReturnFilteredItems() {
        String search = "Product";
        ItemsSort sort = ItemsSort.PRICE;
        int pageNumber = 2;
        int pageSize = 10;
        List<ItemDto> items = List.of(
                new ItemDto(10L, "Product10", "Desc10", "img10.jpg", 500L, 1)
        );
        PageItemDto page = new PageItemDto();
        page.setContent(items);
        when(itemService.findItems(search, sort, pageNumber-1, pageSize)).thenReturn(Mono.just(page));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/items")
                        .queryParam("search", search)
                        .queryParam("sort", sort.name())
                        .queryParam("pageNumber", String.valueOf(pageNumber))
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class).value(
                        body -> Assertions.assertThat(body)
                        .isNotEmpty()
                        .contains("items")
                        .contains(search));

        verify(itemService, times(1)).findItems(search, sort, pageNumber-1, pageSize);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    void postItems_shouldExecuteActionAndRedirectWithAllParameters() {
        Long id = 1L;
        String search = "test";
        ItemsSort sort = ItemsSort.PRICE;
        int pageNumber = 3;
        int pageSize = 7;
        ItemAction action = ItemAction.PLUS;

        when(cartService.executeAction(id, action)).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/items")
                .body(BodyInserters.fromFormData("id", id.toString())
                        .with("search", search)
                        .with("sort", sort.name())
                        .with("pageNumber", String.valueOf(pageNumber))
                        .with("pageSize", String.valueOf(pageSize))
                        .with("action", action.name()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/items?search=" + search + "&pageNumber=" + pageNumber +
                        "&sort=" + sort.name() + "&pageSize=" + pageSize
                );

        verify(cartService, times(1)).executeAction(id, action);
        verifyNoMoreInteractions(cartService);
        verifyNoInteractions(itemService);
    }

    @Test
    void getItemById_shouldReturnItemView() {
        Long id = 5L;
        ItemDto item = new ItemDto(id, "Special", "Desc", "img.jpg", 999L, 1);
        when(itemService.getItemById(id)).thenReturn(Mono.just(item));

        webTestClient.get()
                .uri("/items/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class).value(
                        body -> Assertions.assertThat(body)
                        .isNotEmpty()
                        .contains("items"));

        verify(itemService, times(1)).getItemById(id);
        verifyNoMoreInteractions(itemService);
        verifyNoInteractions(cartService);
    }

    @Test
    void postItemById_shouldExecuteActionAndReturnItemViewWithUpdatedItem() {
        Long id = 7L;
        ItemAction action = ItemAction.DELETE;
        ItemDto updatedItem = new ItemDto(id, "Updated", "NewDesc", "new.jpg", 100L, 0);
        when(itemService.getItemById(id)).thenReturn(Mono.just(updatedItem));
        when(cartService.executeAction(id, action)).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/items/{id}", id)
                .body(BodyInserters.fromFormData("action", action.name()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/items/" + id);

        verify(cartService, times(1)).executeAction(id, action);
        verifyNoMoreInteractions(cartService, itemService);
    }
}