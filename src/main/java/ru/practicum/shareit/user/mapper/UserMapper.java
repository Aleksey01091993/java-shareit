package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDTO userDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserResponseDTO userDTO) {
        return new User(
                null,
                userDTO.getName(),
                userDTO.getEmail()
        );
    }
}
