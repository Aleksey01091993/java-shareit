package ru.practicum.ItemRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ItemRequest.DTO.ItemRequestCreateRequestDto;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(ItemRequestCreateRequestDto itemRequest, Long userId) {
        return post("", userId, itemRequest);
    }

    public ResponseEntity<Object> findByAll(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllFrom(Long userId, Integer from, Integer size) {
        if (from != null && size != null) {
            Map<String, Object> parameters = Map.of(
                    "from", from,
                    "size", size
            );
            return get("/all?from={from}&size={size}", userId, parameters);
        }
        return get("/all", userId);

    }

    public ResponseEntity<Object> findById(Long userId, Long requestId) {
        Map<String, Object> parameters = Map.of(
                "requestId", requestId
        );
        return get("/{requestId}", userId, parameters);
    }


}
