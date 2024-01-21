package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestToItem;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems())
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestCreateRequestDto itemRequestCreateRequestDto, User user, List<ItemRequestToItem> items) {
        return new ItemRequest
                (
                        null,
                        itemRequestCreateRequestDto.getDescription(),
                        user,
                        LocalDateTime.now(),
                        items

                );
    }

    public static ItemRequestToItem toItemRequestToItem(Item item) {
        return new ItemRequestToItem(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest().getId()
        );
    }
}
