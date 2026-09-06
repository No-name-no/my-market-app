package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.mnuykin.client.api.PaymentApi;
import org.mnuykin.client.domain.ExecuteResponse;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class OrderServiceTest extends BaseServiceTest{

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentApi paymentApi;

    @Test
    void buy(){
        ExecuteResponse response = new ExecuteResponse();
        response.setStatus(ExecuteResponse.StatusEnum.SUCCESSFUL);
        response.setRemainingBalance(new BigDecimal("100.00"));
        when(paymentApi.executePayment(anyString(),any())).thenReturn(Mono.just(ResponseEntity.ok(response)));

        cartService.executeAction(id, ItemAction.PLUS).block();
        cartService.executeAction(id, ItemAction.PLUS).block();
        OrderDto orderDto = orderService.create().block();

        assertNotNull(orderDto);
        assertEquals(price*2,orderDto.getTotalSum());
        assertFalse(orderDto.getItems().isEmpty());
        assertEquals(1, orderDto.getItems().size());
        ItemDto itemDto = orderDto.getItems().getFirst();
        assertNotNull(itemDto);
        assertEquals(title, itemDto.getTitle());
        assertEquals(2, itemDto.getCount());
        assertEquals(price, itemDto.getPrice());

        List<OrderDto> orderDtos = orderService.getOrder().collectList().block();
        assertNotNull(orderDtos);
        assertFalse(orderDtos.isEmpty());
        OrderDto getOrder = orderDtos.getFirst();
        assertEquals(getOrder, orderDto);

        List<ItemDto> itemDtoList = cartService.getItems().collectList().block();
        assertNotNull(itemDtoList);
        assertTrue(itemDtoList.isEmpty());
    }
}