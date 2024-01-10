package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;

@Service
public class ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;

    public ItemRequestService(@Autowired ItemRequestStorage itemRequestStorage,
                              @Autowired UserStorage userStorage) {
        this.itemRequestStorage = itemRequestStorage;
        this.userStorage = userStorage;
    }

    public ItemRequest create(ItemRequestCreateRequestDto itemRequest, Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));
        ItemRequest saveItemRequest = new ItemRequest(
                null,
                itemRequest.getDescription(),
                user,
                LocalDateTime.now()
        );
        return itemRequestStorage.save(saveItemRequest);
    }


}
