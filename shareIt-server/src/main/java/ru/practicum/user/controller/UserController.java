package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserRequestDTO;
import ru.practicum.user.dto.UserResponseDTO;
import ru.practicum.user.service.UserService;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j

@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody UserRequestDTO user
    ) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        UserResponseDTO userResponse = service.create(user);
        log.info("Отправлен ответ для POST запроса /users с телом: {}", userResponse);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(
            @RequestBody @Nullable UserRequestDTO user,
            @PathVariable Long userId
    ) {
        log.info("Пришел PATH запрос /users/{} с телом: {}", userId, user);
        UserResponseDTO userResponse = service.update(user, userId);
        log.info("Отправлен ответ для PATH запроса /users/{} с телом: {}", userId, userResponse);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(
            @PathVariable Long userId
    ) {
        log.info("Пришел GET запрос /users с телом: {}", userId);
        UserResponseDTO userResponse = service.get(userId);
        log.info("Отправлен ответ для GET запроса /users с телом: {}", userResponse);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(
            @PathVariable(required = false) Long userId
    ) {
        log.info("Пришел DELETE запрос /users/{}", userId);
        UserResponseDTO userResponse = service.delete(userId);
        log.info("Отправлен ответ для DELETE запроса с телом: {}", userResponse);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Пришел GET запрос /users");
        List<UserResponseDTO> userResponse = service.getAll();
        log.info("Отправлен ответ для GET запроса /users");
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
