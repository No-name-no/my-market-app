package org.mnuykin.mymarket.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mnuykin.mymarket.entity.Order;
import org.mnuykin.mymarket.mapper.OrderMapper;
import org.mnuykin.mymarket.model.OrderDto;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.repository.OrderRepository;
import org.mnuykin.mymarket.service.impl.OrderServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void getOrder_shouldReturnListOfOrder() {
        List<Order> orders = List.of(new Order(), new Order());
        List<OrderDto> expectedDtos = List.of(new OrderDto(), new OrderDto());
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toListDto(orders)).thenReturn(expectedDtos);

        List<OrderDto> result = orderService.getOrder();

        assertEquals(expectedDtos, result);
        verify(orderRepository).findAll();
        verify(orderMapper).toListDto(orders);
        verifyNoMoreInteractions(orderRepository, orderMapper);
        verifyNoInteractions(itemRepository);
    }

    @Test
    void getOrderById_shouldReturnOrderDto() {
        Long id = 1L;
        Order order = new Order();
        order.setId(id);
        OrderDto expectedDto = new OrderDto();
        expectedDto.setId(id);
        when(orderRepository.getOrderById(id)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(expectedDto);

        OrderDto result = orderService.getOrderById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(orderRepository).getOrderById(id);
        verify(orderMapper).toDto(order);
        verifyNoMoreInteractions(orderRepository, orderMapper);
        verifyNoInteractions(itemRepository);
    }

    @Test
    void getOrderById_shouldThrowException() {
        Long id = 2L;
        when(orderRepository.getOrderById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(id));
        verify(orderRepository).getOrderById(id);
        verifyNoInteractions(orderMapper, itemRepository);
    }

    /*@Test
    void create_shouldCreateOrderFromCartItemsAndClearCart() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setPrice(100L);
        item1.setCount(2);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setPrice(50L);
        item2.setCount(3);

        List<Item> cartItems = List.of(item1, item2);
        when(itemRepository.findByCountGreaterThan(0)).thenReturn(cartItems);

        Order savedOrder = new Order();
        savedOrder.setId(10L);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderDto expectedDto = new OrderDto();
        expectedDto.setId(10L);
        when(orderMapper.toDto(savedOrder)).thenReturn(expectedDto);

        OrderDto result = orderService.create();

        assertNotNull(result);
        assertEquals(10L, result.getId());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();

        assertEquals(350L, capturedOrder.getTotalSum());
        assertNotNull(capturedOrder.getItems());
        assertEquals(2, capturedOrder.getItems().size());

        OrderItem oi1 = capturedOrder.getItems().getFirst();
        assertEquals(item1, oi1.getItem());
        assertEquals(100L, oi1.getPrice());
        assertEquals(2, oi1.getQuantity());

        OrderItem oi2 = capturedOrder.getItems().get(1);
        assertEquals(item2, oi2.getItem());
        assertEquals(50L, oi2.getPrice());
        assertEquals(3, oi2.getQuantity());

        verify(itemRepository).clearCart();
        verify(orderMapper).toDto(savedOrder);
        verifyNoMoreInteractions(orderRepository, itemRepository, orderMapper);
    }

    @Test
    void create_shouldCreateEmptyOrder() {
        when(itemRepository.findByCountGreaterThan(0)).thenReturn(List.of());

        Order savedOrder = new Order();
        savedOrder.setId(20L);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderDto expectedDto = new OrderDto();
        expectedDto.setId(20L);
        when(orderMapper.toDto(savedOrder)).thenReturn(expectedDto);

        OrderDto result = orderService.create();

        assertNotNull(result);
        assertEquals(20L, result.getId());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();

        assertEquals(0L, capturedOrder.getTotalSum());
        assertTrue(capturedOrder.getItems().isEmpty());

        verify(itemRepository).clearCart();
        verify(orderMapper).toDto(savedOrder);
        verifyNoMoreInteractions(orderRepository, itemRepository, orderMapper);
    }*/
}