package ru.practicum.shareit.request;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;
import java.util.stream.Collectors;


@Component
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public ItemRequestController(@Autowired ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping()
    public ItemRequestResponseDto create(@RequestBody @Valid ItemRequestCreateRequestDto requestDto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemRequestResponseDto itemRequestResponseDto =
                ItemRequestMapper.toItemRequestResponseDto(itemRequestService.create(requestDto, userId));
        return itemRequestResponseDto;
    }

    @GetMapping
    public List<ItemRequestResponseDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.findByAll(userId).stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
    }
    @GetMapping("/all")
    public List<ItemRequestResponseDto> findAllFrom(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
        return itemRequestService.getAllFrom(userId, from, size).stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto findById(@PathVariable Long requestId,
                                           @RequestHeader("X-Sharer-User-Id") Long userId){
        return ItemRequestMapper.toItemRequestResponseDto(itemRequestService.findById(userId, requestId));

    }
}
