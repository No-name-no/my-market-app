package org.mnuykin.mymarket.service;

import org.mnuykin.mymarket.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrder();
    Order getOrderById(Long id);
    Order create();
}
