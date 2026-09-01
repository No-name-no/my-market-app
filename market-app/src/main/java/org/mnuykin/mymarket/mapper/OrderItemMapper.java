package org.mnuykin.mymarket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.entity.OrderItem;
import org.mnuykin.mymarket.model.ItemDto;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "imgPath", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "count", source = "orderItem.quantity")
    @Mapping(target = "price", source = "orderItem.price")
    @Mapping(target = "title", source = "item.title")
    ItemDto toDto(OrderItem orderItem, Item item);

    default List<ItemDto> toDtoList(List<OrderItem> orderItems, Map<Long, Item> itemMap){
        return orderItems.stream().map(orderItem -> {
            Item item = itemMap.get(orderItem.getItemId());
            return toDto(orderItem, item);
        }).toList();
    }
}