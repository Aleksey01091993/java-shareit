package ru.practicum.shareit.unitContollerTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
public class UserControllerUnitTests {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;


    private static User userOne;

    private static UserResponseDTO userTwo;


    @BeforeEach
    void addUsers() {
        userOne = User.builder()
                .id(1L)
                .name("nullId")
                .email("nullId@email.ru")
                .build();

        userTwo = UserResponseDTO.builder()
                .name("nullId")
                .email("nullId@email.ru")
                .build();

    }

    @Test
    public void createUser() throws Exception {
        when(userService.create(any()))
                .thenReturn(userOne);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userTwo))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("nullId")))
                .andExpect(jsonPath("$.email", is("nullId@email.ru")));

    }

    @Test
    public void updateUser() throws Exception {
        when(userService.update(any(), anyLong())).thenReturn(userOne);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userTwo))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("nullId")))
                .andExpect(jsonPath("$.email", is("nullId@email.ru")));
    }

    @Test
    public void getUser() throws Exception {
        when(userService.get(anyLong())).thenReturn(userOne);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("nullId")))
                .andExpect(jsonPath("$.email", is("nullId@email.ru")));
    }

    @Test
    public void deleteUser() throws Exception {
        when(userService.delete(anyLong())).thenReturn(userOne);

        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("nullId")))
                .andExpect(jsonPath("$.email", is("nullId@email.ru")));
    }

    @Test
    public void getAllUser() throws Exception {
        when(userService.getAll()).thenReturn(List.of(userOne));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].name", is("nullId")))
                .andExpect(jsonPath("$[0].email", is("nullId@email.ru")));
    }


}
