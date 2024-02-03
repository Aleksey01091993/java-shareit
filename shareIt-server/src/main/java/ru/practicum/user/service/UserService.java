package ru.practicum.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.EmailException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserRequestDTO;
import ru.practicum.user.dto.UserResponseDTO;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserService {

    private final UserStorage storage;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody UserRequestDTO user
    ) {
        User saveUser = storage.save(UserMapper.toUser(user));
        return new ResponseEntity<>(UserMapper.responseDTO(saveUser), HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(
            @RequestBody @Nullable UserRequestDTO user,
            @PathVariable Long userId
    ) {
        User newUser = storage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        if (user.getName() != null) newUser.setName(user.getName());
        if (user.getEmail() != null) newUser.setEmail(user.getEmail());
        User saveUser = storage.save(newUser);
        return new ResponseEntity<>(UserMapper.responseDTO(saveUser), HttpStatus.OK);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable Long userId) {
        User newUser = storage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        return new ResponseEntity<>(UserMapper.responseDTO(newUser), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable(required = false) Long userId) {
        if (userId == null) {
            throw new EmailException("Пустой запрос на удоление");
        }
        User newUser = storage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        storage.delete(newUser);
        return new ResponseEntity<>(UserMapper.responseDTO(newUser), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<UserResponseDTO> users = storage.findAll()
                .stream().map(UserMapper::responseDTO).collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}