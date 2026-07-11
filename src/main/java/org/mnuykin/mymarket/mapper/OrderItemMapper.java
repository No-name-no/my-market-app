package org.mnuykin.mymarket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mnuykin.mymarket.entity.OrderItem;
import org.mnuykin.mymarket.model.ItemDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "imgPath", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "count", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "title", expression = "java(item.getItem().getTitle())")
    ItemDto toDto(OrderItem item);
    List<ItemDto> toDto(List<OrderItem> item);
}