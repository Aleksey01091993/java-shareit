package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestCreateRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestToItem;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    public ItemRequestService(@Autowired ItemRequestStorage itemRequestStorage,
                              @Autowired UserStorage userStorage,
                              @Autowired ItemStorage itemStorage) {
        this.itemRequestStorage = itemRequestStorage;
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }

    public ItemRequest create(ItemRequestCreateRequestDto itemRequest, Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));
        return itemRequestStorage.save(ItemRequestMapper.toItemRequest(itemRequest, user, null));
    }

    public List<ItemRequest> findByAll(Long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));
        return itemRequestStorage.findByRequestor_IdOrderByCreatedAsc(userId).stream()
                .peek(o1 -> o1.setItems(
                        itemStorage.findByRequest_Id(o1.getId()).stream()
                                .map(ItemRequestMapper::toItemRequestToItem)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public List<ItemRequest> getAllFrom(Long userId, Integer from, Integer size) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));
        if (from == null || size == null) {
            return new ArrayList<>();
        }

        return itemRequestStorage.findByRequestor_IdNotOrderByCreatedAsc(userId, PageRequest.of(from / size, size))
                .stream()
                .peek(o1 -> o1.setItems(
                        itemStorage.findByRequest_Id(o1.getId()).stream()
                                .map(ItemRequestMapper::toItemRequestToItem)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public ItemRequest findById(Long userId, Long requestId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));

        ItemRequest itemRequest = itemRequestStorage.findById(requestId)
                .orElseThrow(() -> new NotFoundException("request not found by id:" + userId));

        itemRequest.setItems(itemStorage.findByRequest_Id(requestId).stream()
                .map(ItemRequestMapper::toItemRequestToItem)
                .collect(Collectors.toList()));
        return itemRequest;
    }






}
