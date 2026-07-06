package org.mnuykin.mymarket.service;

import org.mnuykin.mymarket.model.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrder();
    OrderDto getOrderById(Long id);
    OrderDto create();
}
