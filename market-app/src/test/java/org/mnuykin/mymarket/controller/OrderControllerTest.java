package org.mnuykin.mymarket.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.model.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class OrderControllerTest extends BaseControllerTest{

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser(username = "test")
    void getOrders_shouldReturnOrdersViewWithOrders() {
        when(orderService.getOrder()).thenReturn(Flux.just(
                new OrderDto(1L, 1000L, List.of()),
                new OrderDto(2L, 2000L, List.of()))
        );

        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class).value(body -> Assertions.assertThat(body)
                        .isNotEmpty()
                        .contains("order")
                );

        verify(orderService, times(1)).getOrder();
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void getOrders_anonymous_shouldRedirectLogin() {
        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/login");
    }

    @Test
    @WithMockUser(username = "test")
    void getOrder_shouldReturnOrderViewWithOrderAndNewOrderFlag() {
        Long id = 1L;
        boolean newOrder = true;
        when(orderService.getOrderById(id)).thenReturn(Mono.just(new OrderDto(id, 1500L, List.of())));
        webTestClient.get().uri("/orders/{id}?newOrder={newOrder}", id, newOrder)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).value(
                        body -> Assertions.assertThat(body)
                        .isNotEmpty()
                        .contains("order")
                );

        verify(orderService, times(1)).getOrderById(id);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void getOrder_anonymous_shouldRedirectLogin() {
        Long id = 1L;
        boolean newOrder = true;
        webTestClient.get().uri("/orders/{id}?newOrder={newOrder}", id, newOrder)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/login");
    }

    @Test
    @WithMockUser(username = "test")
    void getOrder_shouldReturnOrderViewWithNewOrderFalseByDefault() {
        Long id = 2L;
        when(orderService.getOrderById(id)).thenReturn(Mono.just(new OrderDto(id, 3000L, List.of())));

        webTestClient.get().uri("/orders/{id}", id).exchange()
                .expectStatus().isOk()
                .expectBody(String.class).value(
                        body -> Assertions.assertThat(body)
                        .isNotEmpty()
                        .contains("order")
                );

        verify(orderService, times(1)).getOrderById(id);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @WithMockUser(username = "test")
    void buy_shouldCreateOrderAndRedirectToOrderWithNewOrderFlag() {
        long createdId = 10L;
        when(orderService.create()).thenReturn(Mono.just(new OrderDto(createdId, 5000L, List.of())));

        webTestClient.post().uri("/buy")
                .exchange().expectStatus().is3xxRedirection()
                .expectHeader().location("/orders/" + createdId + "?newOrder=true");

        verify(orderService, times(1)).create();
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void buy_anonymous_shouldRedirectLogin() {
        long createdId = 10L;
        when(orderService.create()).thenReturn(Mono.just(new OrderDto(createdId, 5000L, List.of())));

        webTestClient.post().uri("/buy")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/login");
    }
}