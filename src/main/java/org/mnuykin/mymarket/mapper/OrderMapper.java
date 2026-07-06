package org.mnuykin.mymarket.mapper;

import org.mapstruct.Mapper;
import org.mnuykin.mymarket.entity.Order;
import org.mnuykin.mymarket.model.OrderDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto (Order order);
    List<OrderDto> toListDto (List<Order> orders);
}
