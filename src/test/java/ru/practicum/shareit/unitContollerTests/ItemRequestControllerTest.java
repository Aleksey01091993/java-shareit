package ru.practicum.shareit.unitContollerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService service;

    @Autowired
    private MockMvc mvc;

    private static ItemRequest itemRequest;


    @BeforeEach
    void addItemRequest() {
        itemRequest = new ItemRequest(
                1L,
                "text",
                new User(1L, "name", "email"),
                null,
                null
                );
    }

    @Test
    public void create() throws Exception {
        when(service.create(any(), anyLong()))
                .thenReturn(itemRequest);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.description", is("text")))
                .andExpect(jsonPath("$.created", is(nullValue())))
                .andExpect(jsonPath("$.items", is(nullValue())));

    }

    @Test
    public void findByAll() throws Exception {
        when(service.findByAll(anyLong()))
                .thenReturn(List.of(itemRequest));

        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].description", is("text")))
                .andExpect(jsonPath("$[0].created", is(nullValue())))
                .andExpect(jsonPath("$[0].items", is(nullValue())));

    }

    @Test
    public void findByAllFrom() throws Exception {
        when(service.getAllFrom(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequest));

        mvc.perform(get("/requests/all?from=1&size=1")
                        .content(mapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].description", is("text")))
                .andExpect(jsonPath("$[0].created", is(nullValue())))
                .andExpect(jsonPath("$[0].items", is(nullValue())));

    }

    @Test
    public void findById() throws Exception {
        when(service.findById(anyLong(), anyLong()))
                .thenReturn(itemRequest);

        mvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.description", is("text")))
                .andExpect(jsonPath("$.created", is(nullValue())))
                .andExpect(jsonPath("$.items", is(nullValue())));

    }
}
