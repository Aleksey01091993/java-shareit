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
    public ItemRequestResponseDto create
            (
                    @RequestBody @Valid ItemRequestCreateRequestDto requestDto,
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел POST запрос /requests с телом: {}", requestDto);
        ItemRequestResponseDto itemRequestResponseDto =
                ItemRequestMapper.toItemRequestResponseDto(itemRequestService.create(requestDto, userId));
        log.info("Отправлен ответ для POST запроса /requests с телом: {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }

    @GetMapping
    public List<ItemRequestResponseDto> findAll
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел GET запрос /requests с : X-Sharer-User-Id {}", userId);
        List<ItemRequestResponseDto> itemRequestResponseDto = itemRequestService.findByAll(userId).stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
        log.info("Отправлен ответ для GET запроса /requests с телом: {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }
    @GetMapping("/all")
    public List<ItemRequestResponseDto> findAllFrom
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestParam @Nullable Integer from,
                    @RequestParam @Nullable Integer size
            ) {
        log.info("Пришел GET запрос /requests с параметрами: {}, {}, {}", userId, from, size);
        List<ItemRequestResponseDto> itemRequestResponseDto = itemRequestService.getAllFrom(userId, from, size).stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
        log.info("Отправлен ответ для GET запроса /requests с телом: {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto findById
            (
                    @PathVariable Long requestId,
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел GET запрос /requests с параметрами: {}", requestId);
        ItemRequestResponseDto itemRequestResponseDto = ItemRequestMapper.toItemRequestResponseDto(itemRequestService.findById(userId, requestId));
        log.info("Отправлен ответ для GET запроса /requests с телом: {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }
}
