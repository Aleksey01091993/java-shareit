package ru.practicum.shareit.unitContollerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.coments.DTO.CommentsDTO;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.dto.BookingToItem;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestToItem;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerUnitTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService service;

    @Autowired
    private MockMvc mvc;

    private static ItemResponseDto itemResponseDto;
    private static Item item;
    private static Comments comments;
    private static CommentsDTO commentsDTO;

    @BeforeEach
    void addItems() {
        itemResponseDto = new ItemResponseDto(
                1L,
                "name",
                "description",
                true,
                new BookingToItem(1L, 2L),
                new BookingToItem(3L, 4L),
                2L,
                List.of(new CommentsDTO(1L,
                        "coments",
                        "name",
                        LocalDateTime.now()))
        );

        item = new Item(
                1L,
                "name",
                "description",
                true,
                new BookingToItem(1L, 2L),
                new BookingToItem(3L, 4L),
                new User(2L, "user", "user@email.ru"),
                new ItemRequest(2L,
                        "description",
                        new User(2L,
                                "user",
                                "user@email.ru"),
                        LocalDateTime.now(),
                        List.of(new ItemRequestToItem(
                                1L,
                                "name",
                                "description",
                                true,
                                2L
                        ))),
                null
        );

        comments = new Comments(
                1L,
                "text",
                1L,
                new User(
                        1L,
                        "name",
                        "email@email"),
                LocalDateTime.now());
        commentsDTO = new CommentsDTO(
                1L,
                "text",
                "name",
                LocalDateTime.now());

    }

    @Test
    public void createItems() throws Exception {
        when(service.create(any(), anyLong())).thenReturn(item);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(itemResponseDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemResponseDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.id", is(1)))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(2)))
                .andExpect(jsonPath("$.nextBooking.id", is(3)))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(4)))
                .andExpect(jsonPath("$.requestId", is(2)))
                .andExpect(jsonPath("$.comments", is(nullValue())));

    }

    @Test
    public void updateItems() throws Exception {
        when(service.update(any(), anyLong(), anyLong())).thenReturn(item);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(item))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(itemResponseDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemResponseDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.id", is(1)))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(2)))
                .andExpect(jsonPath("$.nextBooking.id", is(3)))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(4)))
                .andExpect(jsonPath("$.requestId", is(2)))
                .andExpect(jsonPath("$.comments", is(nullValue())));

    }

    @Test
    public void getItem() throws Exception {
        when(service.get(anyLong(), anyLong())).thenReturn(item);

        mvc.perform(get("/items/1")
                        .content(mapper.writeValueAsString(item))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(itemResponseDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemResponseDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.id", is(1)))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(2)))
                .andExpect(jsonPath("$.nextBooking.id", is(3)))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(4)))
                .andExpect(jsonPath("$.requestId", is(2)))
                .andExpect(jsonPath("$.comments", is(nullValue())));

    }

    @Test
    public void getAllItem() throws Exception {
        when(service.getAll(anyLong())).thenReturn(List.of(item));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemResponseDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemResponseDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemResponseDto.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(1)))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(2)))
                .andExpect(jsonPath("$[0].nextBooking.id", is(3)))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(4)))
                .andExpect(jsonPath("$[0].requestId", is(2)))
                .andExpect(jsonPath("$[0].comments", is(nullValue())));

    }

    @Test
    public void getAllSearchItem() throws Exception {
        when(service.getAllSearch(anyString())).thenReturn(List.of(item));

        mvc.perform(get("/items/search?text=text")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemResponseDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemResponseDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemResponseDto.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(1)))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(2)))
                .andExpect(jsonPath("$[0].nextBooking.id", is(3)))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(4)))
                .andExpect(jsonPath("$[0].requestId", is(2)))
                .andExpect(jsonPath("$[0].comments", is(nullValue())));

    }

    @Test
    public void addComments() throws Exception {
        when(service.addComment(anyLong(), anyLong(), any())).thenReturn(comments);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comments))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentsDTO.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentsDTO.getText())))
                .andExpect(jsonPath("$.authorName", is(commentsDTO.getAuthorName())));

    }
}
