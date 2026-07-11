package org.mnuykin.mymarket.mapper;

import org.mapstruct.Mapper;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.model.ItemDto;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto toDto(Item item);
    List<ItemDto> toDtoList(List<Item> list);
}
