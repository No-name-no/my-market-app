package org.mnuykin.mymarket.mapper;

import org.mapstruct.Mapper;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.model.ItemDto;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto toDto(Item item);
    Page<ItemDto> toDtoPage(Page<Item> page);
}
