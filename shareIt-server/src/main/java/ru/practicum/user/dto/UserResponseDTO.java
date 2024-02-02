package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
}
