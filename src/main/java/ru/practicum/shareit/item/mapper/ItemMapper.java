package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDTO toItemDto(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

}
