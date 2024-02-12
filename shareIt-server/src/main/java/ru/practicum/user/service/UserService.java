package ru.practicum.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
@RequiredArgsConstructor
public class UserService {

    private final UserStorage storage;


    public UserResponseDTO create(
            UserRequestDTO user
    ) {
        User saveUser = storage.save(UserMapper.toUser(user));
        return UserMapper.responseDTO(saveUser);
    }


    public UserResponseDTO update(
            UserRequestDTO user,
            Long userId
    ) {
        User newUser = storage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        if (user.getName() != null) newUser.setName(user.getName());
        if (user.getEmail() != null) newUser.setEmail(user.getEmail());
        User saveUser = storage.save(newUser);
        return UserMapper.responseDTO(saveUser);

    }


    public UserResponseDTO get(Long userId) {
        User newUser = storage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        return UserMapper.responseDTO(newUser);
    }


    public UserResponseDTO delete(
            Long userId
    ) {
        if (userId == null) {
            throw new EmailException("Пустой запрос на удоление");
        }
        User newUser = storage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        storage.delete(newUser);
        return UserMapper.responseDTO(newUser);
    }


    public List<UserResponseDTO> getAll() {
        List<UserResponseDTO> users = storage.findAll()
                .stream().map(UserMapper::responseDTO).collect(Collectors.toList());
        return users;
    }
}