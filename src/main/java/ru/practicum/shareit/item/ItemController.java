package ru.practicum.shareit.item;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFound404;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ItemDTO create(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long userId) throws Exception {
        log.info("Пришел POST запрос /items с телом: {}", item);
        ItemDTO itemDTO = service.create(item, userId);
        log.info("Отправлен ответ для POST запроса /items с телом: {}", itemDTO);
        return itemDTO;
    }

    @PatchMapping("/{itemId}")
    public ItemDTO update(@RequestBody @Nullable Item item,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable Long itemId) throws Exception {
        log.info("Пришел PATH запрос /users/{} с телом: {}", itemId, item);
        ItemDTO itemDTO = service.update(item, userId, itemId);
        log.info("Отправлен ответ для PATH запроса /users/{} с телом: {}", itemDTO, itemDTO);
        return itemDTO;
    }
}
