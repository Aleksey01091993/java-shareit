package ru.practicum.request.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.storage.ItemStorage;
import ru.practicum.request.dto.ItemRequestCreateRequestDto;
import ru.practicum.request.dto.ItemRequestResponseDto;
import ru.practicum.request.mapper.ItemRequestMapper;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.storage.ItemRequestStorage;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;


    public ItemRequestResponseDto create(
            ItemRequestCreateRequestDto itemRequest,
            Long userId

    ) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        ItemRequest request = itemRequestStorage.save(ItemRequestMapper.toItemRequest(itemRequest, user, null));
        return ItemRequestMapper.toItemRequestResponseDto(request);
    }


    public List<ItemRequestResponseDto> findByAll(
            Long userId

    ) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        List<ItemRequestResponseDto> response = itemRequestStorage.findByRequestor_IdOrderByCreatedAsc(userId).stream()
                .peek(o1 -> o1.setItems(
                        itemStorage.findByRequest_Id(o1.getId()).stream()
                                .map(ItemRequestMapper::toItemRequestToItem)
                                .collect(Collectors.toList())
                ))
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
        return response;
    }


    public List<ItemRequestResponseDto> getAllFrom(
            Long userId,
            Integer from,
            Integer size
    ) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));
        List<ItemRequestResponseDto> response;
        if (from == null || size == null) {
            return Collections.emptyList();
        }

        response = itemRequestStorage.findByRequestor_IdNotOrderByCreatedAsc(userId, PageRequest.of(from / size, size))
                .stream()
                .peek(o1 -> o1.setItems(
                        itemStorage.findByRequest_Id(o1.getId()).stream()
                                .map(ItemRequestMapper::toItemRequestToItem)
                                .collect(Collectors.toList())
                ))
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
        return response;
    }


    public ItemRequestResponseDto findById(
            Long requestId,
            Long userId
    ) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));

        ItemRequest itemRequest = itemRequestStorage.findById(requestId)
                .orElseThrow(() -> new NotFoundException("request not found by id:" + userId));

        itemRequest.setItems(itemStorage.findByRequest_Id(requestId).stream()
                .map(ItemRequestMapper::toItemRequestToItem)
                .collect(Collectors.toList()));
        return ItemRequestMapper.toItemRequestResponseDto(itemRequest);
    }






}
