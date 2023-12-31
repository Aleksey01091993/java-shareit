package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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

    public static Item toItem(ItemDTO itemDTO, User user) {
        Item newItem = new Item();
        newItem.setName(itemDTO.getName());
        newItem.setDescription(itemDTO.getDescription());
        newItem.setAvailable(itemDTO.getAvailable());
        newItem.setOwner(user);
        return newItem;
    }

}
