package ru.practicum.request.service;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.storage.ItemStorage;
import ru.practicum.request.dto.ItemRequestCreateRequestDto;
import ru.practicum.request.mapper.ItemRequestMapper;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.storage.ItemRequestStorage;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserStorage;

import java.util.ArrayList;
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
    public ItemRequest create(
            @RequestBody @Valid ItemRequestCreateRequestDto itemRequest,
            @RequestHeader("X-Sharer-User-Id") Long userId

    ) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        return itemRequestStorage.save(ItemRequestMapper.toItemRequest(itemRequest, user, null));
    }

    @GetMapping
    public List<ItemRequest> findByAll(
            @RequestHeader("X-Sharer-User-Id") Long userId

    ) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        return itemRequestStorage.findByRequestor_IdOrderByCreatedAsc(userId).stream()
                .peek(o1 -> o1.setItems(
                        itemStorage.findByRequest_Id(o1.getId()).stream()
                                .map(ItemRequestMapper::toItemRequestToItem)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<ItemRequest> getAllFrom(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
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

    @GetMapping("/{requestId}")
    public ItemRequest findById(
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
        return itemRequest;
    }






}
