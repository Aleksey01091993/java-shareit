package ru.practicum.request.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;



    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody ItemRequestCreateRequestDto itemRequest,
            @RequestHeader("X-Sharer-User-Id") Long userId

    ) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        ItemRequest request = itemRequestStorage.save(ItemRequestMapper.toItemRequest(itemRequest, user, null));
        return new ResponseEntity<>(ItemRequestMapper.toItemRequestResponseDto(request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> findByAll(
            @RequestHeader("X-Sharer-User-Id") Long userId

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
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllFrom(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));
        if (from == null || size == null) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        List<ItemRequestResponseDto> response = itemRequestStorage.findByRequestor_IdNotOrderByCreatedAsc(userId, PageRequest.of(from / size, size))
                .stream()
                .peek(o1 -> o1.setItems(
                        itemStorage.findByRequest_Id(o1.getId()).stream()
                                .map(ItemRequestMapper::toItemRequestToItem)
                                .collect(Collectors.toList())
                ))
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(
            @PathVariable Long requestId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));

        ItemRequest itemRequest = itemRequestStorage.findById(requestId)
                .orElseThrow(() -> new NotFoundException("request not found by id:" + userId));

        itemRequest.setItems(itemStorage.findByRequest_Id(requestId).stream()
                .map(ItemRequestMapper::toItemRequestToItem)
                .collect(Collectors.toList()));
        return new ResponseEntity<>(ItemRequestMapper.toItemRequestResponseDto(itemRequest), HttpStatus.OK);
    }






}
