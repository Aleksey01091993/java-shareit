package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
public class ItemCreationDTO {
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;

}
