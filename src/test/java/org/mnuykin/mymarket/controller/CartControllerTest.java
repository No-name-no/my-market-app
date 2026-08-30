package org.mnuykin.mymarket.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

class CartControllerTest extends BaseControllerTest {

    @Autowired
    private CartService cartService;

    @Test
    void getCart_shouldReturnCartViewWithItemsAndTotal() throws Exception {
        when(cartService.getItems()).thenReturn(Flux.just(
                new ItemDto(1L, "Product A", "Desc A", "img1.jpg", 100L, 2),
                new ItemDto(2L, "Product B", "Desc B", "img2.jpg", 200L, 1))
        );
        when(cartService.getTotal()).thenReturn(Mono.just(99L));

        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class).value(body -> {
                    Assertions.assertThat(body)
                            .isNotEmpty()
                            .contains("cart");
                });

        verify(cartService, times(1)).getItems();
        verify(cartService, times(1)).getTotal();
        verifyNoMoreInteractions(cartService);
    }


    @Test
    void postCart_shouldExecuteActionAndReturnCartView() throws Exception {
        final Long id = 1L;
        final ItemAction action = ItemAction.PLUS;
        when(cartService.getItems()).thenReturn(Flux.just(
                new ItemDto(1L, "Product A", "Desc A", "img1.jpg", 100L, 2),
                new ItemDto(2L, "Product B", "Desc B", "img2.jpg", 200L, 1))
        );
        when(cartService.getTotal()).thenReturn(Mono.just(99L));
        when(cartService.executeAction(id, action)).thenReturn(Mono.empty());

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/cart/items")
                        .queryParam("id", id.toString())
                        .queryParam("action", action.name())
                        .build())
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/cart/items");

        verify(cartService, times(1)).executeAction(id, action);
        verifyNoMoreInteractions(cartService);
    }
}