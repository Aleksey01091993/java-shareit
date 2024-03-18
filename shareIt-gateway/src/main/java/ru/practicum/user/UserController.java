package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.DTO.UserRequestDTO;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> userCreateDTO
            (
                    @RequestBody @Valid UserRequestDTO user
            ) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        ResponseEntity<Object> userResponse = userClient.createUser(user);
        log.info("Отправлен ответ для POST запроса /users с телом: {}", userResponse);
        return userResponse;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> userUpdate
            (
                    @RequestBody @Nullable UserRequestDTO user,
                    @PathVariable Long userId
            ) {
        log.info("Пришел PATH запрос /users/{} с телом: {}", userId, user);
        ResponseEntity<Object> userResponse = userClient.updateUser(userId, user);
        log.info("Отправлен ответ для PATH запроса /users/{} с телом: {}", userId, userResponse);
        return userResponse;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> userGet
            (
                    @PathVariable Long userId
            ) {
        log.info("Пришел GET запрос /users с телом: {}", userId);
        ResponseEntity<Object> userResponse = userClient.getUser(userId);
        log.info("Отправлен ответ для GET запроса /users с телом: {}", userResponse);
        return userResponse;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> userDelete
            (
                    @PathVariable(required = false) Long userId
            ) {
        log.info("Пришел DELETE запрос /users/{}", userId);
        ResponseEntity<Object> userResponse = userClient.deleteUser(userId);
        log.info("Отправлен ответ для DELETE запроса с телом: {}", userResponse);
        return userResponse;
    }

    @GetMapping
    public ResponseEntity<Object> userGetAll() {
        log.info("Пришел GET запрос /users");
        ResponseEntity<Object> userResponse = userClient.getAllUser();
        log.info("Отправлен ответ для GET запроса /users");
        return userResponse;
    }
}
