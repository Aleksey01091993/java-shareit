package ru.practicum.user.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
public class UserRequestDTO {
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
}
