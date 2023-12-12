package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemCreationDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDTO toItemDto(Item item) {
        return new ItemDTO(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
    public static Item toItem(ItemCreationDTO itemCreationDTO) {
        return new Item(
                itemCreationDTO.getName(),
                itemCreationDTO.getDescription(),
                itemCreationDTO.getAvailable(),
                itemCreationDTO.getOwner(),
                itemCreationDTO.getRequest()
                );
    }
}
