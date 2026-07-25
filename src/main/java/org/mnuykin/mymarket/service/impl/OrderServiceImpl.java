package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.advice.exception.CartEmptyException;
import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.entity.Order;
import org.mnuykin.mymarket.entity.OrderItem;
import org.mnuykin.mymarket.mapper.OrderMapper;
import org.mnuykin.mymarket.model.OrderDto;
import org.mnuykin.mymarket.repository.CartRepository;
import org.mnuykin.mymarket.repository.OrderItemRepository;
import org.mnuykin.mymarket.repository.OrderRepository;
import org.mnuykin.mymarket.repository.dto.CartItemData;
import org.mnuykin.mymarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    final private OrderRepository orderRepository;
    final private CartRepository cartRepository;
    final private OrderMapper orderMapper;
    final private OrderItemRepository orderItemRepository;

    @Autowired
    OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository,
                     OrderItemRepository orderItemRepository,
                     OrderMapper orderMapper){
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<OrderDto> getOrder() {
        return orderRepository.findAll()
                .flatMap(order ->
                        orderItemRepository.findAllByOrderId(order.getId())
                                .collectList()
                                .map(orderItems ->
                                        orderMapper.toDto(order, orderItems)
                                )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<OrderDto> getOrderById(Long id) {
        return orderRepository.getOrderById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(id)))
                .flatMap(order ->
                        orderItemRepository.findAllByOrderId(order.getId())
                        .collectList()
                        .map(orderItems -> orderMapper.toDto(order, orderItems))
                );
    }

    @Override
    @Transactional
    public Mono<OrderDto> create() {
        return cartRepository.findCartItemDataAll()
                .switchIfEmpty(Mono.error(new CartEmptyException()))
                .collectList()
                .flatMap(
                        cartItems -> {
                            return orderRepository.save(new Order(null, cartItems.stream().mapToLong(item -> item.price() * item.count()).sum()))
                                    .flatMap(
                                            saveOrder -> {
                                                List<OrderItem> orderItems = new ArrayList<>();
                                                for(CartItemData item : cartItems){
                                                    OrderItem orderItem = new OrderItem();
                                                    orderItem.setItemId(item.itemId());
                                                    orderItem.setOrderId(saveOrder.getId());
                                                    orderItem.setPrice(item.price());
                                                    orderItem.setQuantity(item.count());

                                                    orderItems.add(orderItem);
                                                }

                                                return orderItemRepository.saveAll(orderItems).then(cartRepository.deleteAll())
                                                        .thenReturn(orderMapper.toDto(saveOrder, orderItems));
                                            }
                                    );
                        }
                );
    }
}