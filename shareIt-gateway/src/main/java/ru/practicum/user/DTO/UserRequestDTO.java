package ru.practicum.user.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserRequestDTO {
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
}
