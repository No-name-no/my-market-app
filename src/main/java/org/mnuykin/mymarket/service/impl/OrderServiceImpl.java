package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.entity.Order;
import org.mnuykin.mymarket.mapper.OrderMapper;
import org.mnuykin.mymarket.model.OrderDto;
import org.mnuykin.mymarket.repository.OrderRepository;
import org.mnuykin.mymarket.service.OrderService;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {

    final private OrderRepository orderRepository;
    final private OrderMapper orderMapper;

    OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper){
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<OrderDto> getOrder() {
        return orderMapper.toListDto(orderRepository.findAll());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Optional<Order> order = orderRepository.getOrderById(id);
        return orderMapper.toDto(order.orElseThrow());
    }

    @Override
    public OrderDto create() {
        return null;
    }
}
