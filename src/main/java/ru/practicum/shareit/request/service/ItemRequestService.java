package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestCreateRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
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

    public List<ItemRequest> findById(Long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));
        List<ItemRequest> itemRequests = itemRequestStorage.findByRequestor_IdOrderByCreatedAsc(userId).stream()
                .peek(o1 -> o1.setItems(
                        itemStorage.findByRequest_Id(o1.getId()).stream()
                                .map(ItemRequestMapper::toItemRequestToItem)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return itemRequests;
    }

    public List<ItemRequest> getAllFrom(Long userId ,Integer from, Integer size) {
        List<Item> userItems = itemStorage.findAllByOwnerId(userId);
        Collection<String> containers = new ArrayList<>();
        for (Item item : userItems) {
            containers.add(item.getName());
            containers.add(item.getDescription());
        }
        Pageable pageable = PageRequest.of(from, size);
        List<ItemRequest> test = itemRequestStorage.findByDescriptionIgnoreCaseInOrderByCreatedAsc(containers, pageable);
        return test;
    }




}
