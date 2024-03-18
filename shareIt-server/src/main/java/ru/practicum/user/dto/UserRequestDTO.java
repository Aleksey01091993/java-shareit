package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserRequestDTO {
    private String name;
    private String email;
}
