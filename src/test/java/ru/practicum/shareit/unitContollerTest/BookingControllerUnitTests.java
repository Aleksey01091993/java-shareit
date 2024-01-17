package ru.practicum.shareit.unitContollerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.dto.BookingToItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
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

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerUnitTests {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService service;

    @Autowired
    private MockMvc mvc;

    private static BookingRequestDto bookingResponseDTO;

    private static Booking booking;

    @BeforeEach
    void addBooking() {

        User user = new User(
                5L,
                "name",
                "email@email");
        ItemRequest itemRequest = new ItemRequest(
                6L, "text", user, null, null);

        Item item = new Item(
                1L,
                "name",
                "text",
                true,
                new BookingToItem(1L, 2L),
                new BookingToItem(3L, 4L),
                user,
                itemRequest,
                null

        );

        booking = new Booking(
                1L,
                null,
                null,
                item,
                user,
                Status.APPROVED


        );
        bookingResponseDTO = new BookingRequestDto(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1)
        );
    }

    @Test
    public void create() throws Exception {
        when(service.create(any(), anyLong()))
                .thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingResponseDTO))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.start", is(nullValue())))
                .andExpect(jsonPath("$.end", is(nullValue())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())));

    }

    @Test
    public void approved() throws Exception {
        when(service.approved(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingResponseDTO))
                        .header("X-Sharer-User-Id", 5)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.start", is(nullValue())))
                .andExpect(jsonPath("$.end", is(nullValue())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())));

    }

    @Test
    public void getBookingId() throws Exception {
        when(service.get(anyLong(), anyLong()))
                .thenReturn(booking);

        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(bookingResponseDTO))
                        .header("X-Sharer-User-Id", 5)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.start", is(nullValue())))
                .andExpect(jsonPath("$.end", is(nullValue())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())));

    }

    @Test
    public void getBookingAll() throws Exception {
        when(service.getAll(anyLong(), anyString()))
                .thenReturn(List.of(booking));

        mvc.perform(get("/bookings?state=state")
                        .content(mapper.writeValueAsString(bookingResponseDTO))
                        .header("X-Sharer-User-Id", 5)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].start", is(nullValue())))
                .andExpect(jsonPath("$[0].end", is(nullValue())))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().toString())))
                .andExpect(jsonPath("$[0].booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(booking.getItem().getName())));

    }

    @Test
    public void getBookingAllOwner() throws Exception {
        when(service.getAllOwnerId(anyLong(), anyString()))
                .thenReturn(List.of(booking));

        mvc.perform(get("/bookings/owner?state=state")
                        .content(mapper.writeValueAsString(bookingResponseDTO))
                        .header("X-Sharer-User-Id", 5)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].start", is(nullValue())))
                .andExpect(jsonPath("$[0].end", is(nullValue())))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().toString())))
                .andExpect(jsonPath("$[0].booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(booking.getItem().getName())));

    }


}
