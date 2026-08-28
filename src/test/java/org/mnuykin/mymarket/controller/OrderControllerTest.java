package org.mnuykin.mymarket.controller;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.model.OrderDto;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest extends BaseControllerTest{
/*

    @Test
    void getOrders_shouldReturnOrdersViewWithOrders() throws Exception {
        // given
        List<OrderDto> orders = List.of(
                new OrderDto(1L, 1000L, List.of()),
                new OrderDto(2L, 2000L, List.of())
        );
        when(orderService.getOrder()).thenReturn(orders);

        // when & then
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attribute("orders", orders));

        verify(orderService, times(1)).getOrder();
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void getOrder_shouldReturnOrderViewWithOrderAndNewOrderFlag() throws Exception {
        // given
        Long id = 1L;
        boolean newOrder = true;
        OrderDto order = new OrderDto(id, 1500L, List.of());
        when(orderService.getOrderById(id)).thenReturn(order);

        // when & then
        mockMvc.perform(get("/orders/{id}", id)
                        .param("newOrder", String.valueOf(newOrder)))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attribute("newOrder", newOrder));

        verify(orderService, times(1)).getOrderById(id);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void getOrder_shouldReturnOrderViewWithNewOrderFalseByDefault() throws Exception {
        // given
        Long id = 2L;
        OrderDto order = new OrderDto(id, 3000L, List.of());
        when(orderService.getOrderById(id)).thenReturn(order);

        // when & then - без параметра newOrder
        mockMvc.perform(get("/orders/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attribute("newOrder", false));

        verify(orderService, times(1)).getOrderById(id);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void buy_shouldCreateOrderAndRedirectToOrderWithNewOrderFlag() throws Exception {
        // given
        long createdId = 10L;
        OrderDto createdOrder = new OrderDto(createdId, 5000L, List.of());
        when(orderService.create()).thenReturn(createdOrder);

        // when & then
        mockMvc.perform(post("/buy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/" + createdId + "?newOrder=true"));

        verify(orderService, times(1)).create();
        verifyNoMoreInteractions(orderService);
    }

 */
}