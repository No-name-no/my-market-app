package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.entity.Order;
import org.mnuykin.mymarket.entity.OrderItem;
import org.mnuykin.mymarket.mapper.OrderMapper;
import org.mnuykin.mymarket.model.OrderDto;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.repository.OrderRepository;
import org.mnuykin.mymarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    final private OrderRepository orderRepository;
    final private ItemRepository itemRepository;
    final private OrderMapper orderMapper;

    @Autowired
    OrderServiceImpl(OrderRepository orderRepository, ItemRepository itemRepository,
                     OrderMapper orderMapper){
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrder() {
        return orderMapper.toListDto(orderRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        Optional<Order> order = orderRepository.getOrderById(id);
        return orderMapper.toDto(order.orElseThrow());
    }

    @Override
    @Transactional
    public OrderDto create() {
        List<Item> items = itemRepository.findByCountGreaterThan(0);

        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        long totalSum = 0L;
        for(Item item : items){
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setPrice(item.getPrice());
            orderItem.setQuantity(item.getCount());
            orderItems.add(orderItem);

            totalSum += orderItem.getPrice() * orderItem.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalSum(totalSum);

        itemRepository.clearCart();
        Order saveOrder = orderRepository.save(order);
        return orderMapper.toDto(saveOrder);
    }
}
