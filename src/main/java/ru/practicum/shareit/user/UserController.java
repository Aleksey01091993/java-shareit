package ru.practicum.shareit.user;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Component
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        User userOne = userService.create(user);
        log.info("Отправлен ответ для POST запроса /users с телом: {}", userOne);
        return userOne;
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody @Nullable User user, @PathVariable Long userId) {
        log.info("Пришел PATH запрос /users/{} с телом: {}", userId, user);
        User userOne = userService.update(user, userId);
        log.info("Отправлен ответ для PATH запроса /users/{} с телом: {}", userId, userOne);
        return userOne;
    }

    @GetMapping("/{userId}")
    public User get(@PathVariable Long userId) {
        log.info("Пришел GET запрос /users с телом: {}", userId);
        User userOne = userService.get(userId);
        log.info("Отправлен ответ для GET запроса /users с телом: {}", userOne);
        return userOne;
    }

    @DeleteMapping("/{userId}")
    public User delete(@PathVariable(required = false) Long userId) {
        log.info("Пришел DELETE запрос /users/{}", userId);
        User user = userService.delete(userId);
        log.info("Отправлен ответ для DELETE запроса с телом: {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Пришел GET запрос /users");
        List<User> users = userService.getAll();
        log.info("Отправлен ответ для GET запроса /users");
        return users;
    }
}
