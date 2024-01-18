package ru.practicum.shareit.unitServiceTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    public void setup() {
        userResponseDTO = new UserResponseDTO("name", "email@email");
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.ru")
                .build();
    }

    @Test
    public void saveUser() {
        when(userStorage.save(any()))
                .thenReturn(user);
        User user = userService.create(userResponseDTO);
        assertEquals(this.user, user);
    }

    @Test
    public void updateUser() {
        when(userStorage.save(any()))
                .thenReturn(user);
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        User user = userService.update(userResponseDTO, 1L);
        assertEquals(this.user, user);
    }

    @Test
    public void userGetId() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        User user = userService.get(1L);
        assertEquals(this.user, user);
    }

    @Test
    public void deleteUserById() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        User user = userService.delete(1L);
        assertEquals(this.user, user);
    }

    @Test
    public void getAllUsers() {
        when(userStorage.findAll())
                .thenReturn(List.of(user));
        List<User> users = userService.getAll();
        assertEquals(users, List.of(user));
    }



}
