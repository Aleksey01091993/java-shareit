package ru.practicum.user.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
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
    public UserResponseDTO create(
            @RequestBody UserRequestDTO user
    ) {
        User saveUser = storage.save(UserMapper.toUser(user));
        return UserMapper.responseDTO(saveUser);
    }

    @PatchMapping("/{userId}")
    public UserResponseDTO update(
            @RequestBody @Nullable UserRequestDTO user,
            @PathVariable Long userId
    ) {
        User newUser = storage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        if (user.getName() != null) newUser.setName(user.getName());
        if (user.getEmail() != null) newUser.setEmail(user.getEmail());
        User saveUser = storage.save(newUser);
        return UserMapper.responseDTO(saveUser);

    }

    @GetMapping("/{userId}")
    public UserResponseDTO get(@PathVariable Long userId) {
        User newUser = storage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        return UserMapper.responseDTO(newUser);
    }

    @DeleteMapping("/{userId}")
    public UserResponseDTO delete(@PathVariable(required = false) Long userId) {
        if (userId == null) {
            throw new EmailException("Пустой запрос на удоление");
        }
        User newUser = storage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        storage.delete(newUser);
        return UserMapper.responseDTO(newUser);
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return storage.findAll()
                .stream().map(UserMapper::responseDTO).collect(Collectors.toList());
    }
}