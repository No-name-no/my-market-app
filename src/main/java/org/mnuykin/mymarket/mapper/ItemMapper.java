package org.mnuykin.mymarket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.model.ItemDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(source = "count", target = "count")
    ItemDto toDto(Item item, Integer count);
}
