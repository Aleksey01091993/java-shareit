package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDTO userDTO(User user) {
        return new UserDTO(
                user.getName()
        );
    }

    public static User toUser(UserCreationDTO userCreationDTO) {
        return new User(
                userCreationDTO.getName(),
                userCreationDTO.getEmail()
        );
    }
}
