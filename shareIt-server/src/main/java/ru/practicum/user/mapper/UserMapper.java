package ru.practicum.user.mapper;


import ru.practicum.user.dto.UserRequestDTO;
import ru.practicum.user.dto.UserResponseDTO;
import ru.practicum.user.model.User;

public class UserMapper {
    public static User toUser(UserRequestDTO userDTO) {
        return new User(
                null,
                userDTO.getName(),
                userDTO.getEmail()
        );
    }

    public static UserResponseDTO responseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
