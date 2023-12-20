package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.user.model.User;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UserDTO extends User {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
}
