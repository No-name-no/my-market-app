package org.mnuykin.mymarket.mapper;

import org.mapstruct.*;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.entity.Order;
import org.mnuykin.mymarket.entity.OrderItem;
import org.mnuykin.mymarket.model.OrderDto;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "items", ignore = true)
    OrderDto toDto(Order order,
                   List<OrderItem> orderItems,
                   Map<Long, Item> items,
                   @Context OrderItemMapper orderItemMapper);

    /**
     * Если посмотрит ревьюер, сделать не получилось через
     * @Mapping(target = "items", expression = "java(orderItemMapper.toDtoList(orderItems, items))")
     * класс ген. без orderItemMapper и добавить как-то в контекст не получалось
     * Поэтому сделал так :(
     */
    @AfterMapping
    default void setItems(@MappingTarget OrderDto orderDto,
                          List<OrderItem> orderItems,
                          Map<Long, Item> items,
                          @Context OrderItemMapper orderItemMapper) {
        orderDto.setItems(orderItemMapper.toDtoList(orderItems, items));
    }
}