package ru.practicum.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.booking.DTO.BookingRequestDto;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";


    public BookingClient(@Value("${shareit-server.url}") String serverUrl,
                         @Autowired RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(BookingRequestDto requestDto, Long userId) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> approvedBooking(Long userId, Long bookingId, String approved) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId,
                "approved", approved
        );
        return patch("/{bookingId}?approved={approved}", userId, parameters);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId
        );
        return get("/{bookingId}", userId, parameters);
    }

    public ResponseEntity<Object> gatAllBooking(Long userId, String state, Integer from, Integer size) {
        if (from != null && size != null) {
            Map<String, Object> parameters = Map.of(
                    "from", from,
                    "size", size
            );
            return get("?from={from}&size={size}", userId, parameters);

        } else if (state == null) {
            return get("", userId);
        }
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getAllOwnerId(Long ownerId, String state, Integer from, Integer size) {
        if (from != null && size != null) {
            Map<String, Object> parameters = Map.of(
                    "from", from,
                    "size", size
            );
            return get("/owner?from={from}&size={size}", ownerId, parameters);
        } else if (state == null) {
            return get("/owner", ownerId);
        }
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("/owner?state={state}", ownerId, parameters);
    }


}
