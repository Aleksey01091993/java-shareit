package ru.practicum.request.mapper;

import ru.practicum.item.model.Item;
import ru.practicum.request.dto.ItemRequestCreateRequestDto;
import ru.practicum.request.dto.ItemRequestResponseDto;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.model.ItemRequestToItem;
import ru.practicum.user.model.User;

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
