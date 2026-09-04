package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.advice.exception.CartEmptyException;
import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.advice.exception.PaymentException;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.entity.Order;
import org.mnuykin.mymarket.entity.OrderItem;
import org.mnuykin.mymarket.mapper.OrderItemMapper;
import org.mnuykin.mymarket.mapper.OrderMapper;
import org.mnuykin.mymarket.model.OrderDto;
import org.mnuykin.mymarket.repository.CartRepository;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.repository.OrderItemRepository;
import org.mnuykin.mymarket.repository.OrderRepository;
import org.mnuykin.mymarket.repository.dto.CartItemData;
import org.mnuykin.mymarket.service.OrderService;
import org.mnuykin.mymarket.service.PaymentService;
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
    final private OrderItemRepository orderItemRepository;
    final private ItemRepository itemRepository;
    final private PaymentService paymentService;

    final private OrderMapper orderMapper;
    final private OrderItemMapper orderItemMapper;

    @Autowired
    OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository,
                     OrderItemRepository orderItemRepository, ItemRepository itemRepository,
                     PaymentService paymentService,
                     OrderMapper orderMapper, OrderItemMapper orderItemMapper){
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<OrderDto> getOrder() {
        return orderRepository.findAll()
                .flatMap(order ->
                        orderItemRepository.findAllByOrderId(order.getId())
                                .collectList()
                                .flatMap(orderItems -> {
                                    List<Long> itemIds = orderItems.stream().map(OrderItem::getItemId).toList();
                                    return itemRepository.findAllById(itemIds)
                                            .collectMap(Item::getId)
                                            .map(items -> orderMapper.toDto(order, orderItems, items, orderItemMapper));
                                })
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
                        .flatMap(orderItems -> {
                            List<Long> itemIds = orderItems.stream().map(OrderItem::getItemId).toList();
                            return itemRepository.findAllById(itemIds)
                                    .collectMap(Item::getId)
                                    .map(items -> orderMapper.toDto(order, orderItems, items, orderItemMapper));
                        })
                );
    }

    @Override
    @Transactional
    public Mono<OrderDto> create() {
        return getCartItemData()
                .flatMap(cartItems -> saveOrder(cartItems)
                        .flatMap(saveOrder -> saveOrderData(saveOrder, cartItems))
                        .flatMap(orderDto ->
                                paymentService.pay(orderDto.getTotalSum())
                                        .flatMap(isSuccessful -> isSuccessful
                                                ? cartRepository.deleteAll().thenReturn(orderDto)
                                                : Mono.error(new PaymentException("Payment error")))
                        )
                );
    }

    private Mono<List<CartItemData>> getCartItemData(){
        return cartRepository.findCartItemDataAll()
                .switchIfEmpty(Mono.error(new CartEmptyException()))
                .collectList();
    }

    private Mono<Order> saveOrder(List<CartItemData> cartItems){
        return orderRepository.save(
                new Order (
                        null, cartItems.stream().mapToLong(
                                item -> item.price() * item.count()).sum()
                )
        );
    }

    private Mono<OrderDto> saveOrderData(Order saveOrder, List<CartItemData> cartItems)
    {
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItemData item : cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(item.item());
            orderItem.setOrderId(saveOrder.getId());
            orderItem.setPrice(item.price());
            orderItem.setQuantity(item.count());

            orderItems.add(orderItem);
        }
        return orderItemRepository.saveAll(orderItems)
                .collectList()
                .flatMap(savedOrderItems -> {
                    List<Long> itemIds = savedOrderItems.stream()
                            .map(OrderItem::getItemId)
                            .toList();

                    return itemRepository.findAllById(itemIds)
                            .collectMap(Item::getId)
                            .map(itemsMap -> orderMapper.toDto(saveOrder, savedOrderItems, itemsMap, orderItemMapper));
                });
    }
}