package ru.practicum.shareit.user;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;


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
    public User userCreateDTO(@RequestBody @Valid UserDTO user) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        UserDTO userResponse = UserMapper.userDTO(userService.create(user));
        log.info("Отправлен ответ для POST запроса /users с телом: {}", userResponse);
        return userResponse;
    }

    @PatchMapping("/{userId}")
    public User userUpdateDTO(@RequestBody @Nullable UserDTO user, @PathVariable Long userId) {
        log.info("Пришел PATH запрос /users/{} с телом: {}", userId, user);
        UserDTO userResponse = UserMapper.userDTO(userService.update(user, userId));
        log.info("Отправлен ответ для PATH запроса /users/{} с телом: {}", userId, userResponse);
        return userResponse;
    }

    @GetMapping("/{userId}")
    public User userGetDTO(@PathVariable Long userId) {
        log.info("Пришел GET запрос /users с телом: {}", userId);
        User userOne = UserMapper.userDTO(userService.get(userId));
        log.info("Отправлен ответ для GET запроса /users с телом: {}", userOne);
        return userOne;
    }

    @DeleteMapping("/{userId}")
    public User userDeleteDTO(@PathVariable(required = false) Long userId) {
        log.info("Пришел DELETE запрос /users/{}", userId);
        User user = UserMapper.userDTO(userService.delete(userId));
        log.info("Отправлен ответ для DELETE запроса с телом: {}", user);
        return user;
    }

    @GetMapping
    public List<User> userGetAllDTO() {
        log.info("Пришел GET запрос /users");
        List<User> users = userService.getAll().stream()
                .map(UserMapper::userDTO)
                .collect(Collectors.toList());
        log.info("Отправлен ответ для GET запроса /users");
        return users;
    }
}
