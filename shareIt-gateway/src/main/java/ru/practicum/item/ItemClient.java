package ru.practicum.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.item.DTO.CommentsDTO;
import ru.practicum.item.DTO.ItemCreateRequestDto;

import java.util.Map;
import java.util.function.Supplier;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory((Supplier<ClientHttpRequestFactory>) HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> itemCreate(ItemCreateRequestDto item, Long userId) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> itemUpdate(ItemCreateRequestDto item, Long userId, Long itemId) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId
        );
        return patch("/{itemId}", userId, parameters, item);
    }

    public ResponseEntity<Object> itemGet(Long userId, Long itemId) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId
        );
        return get("/{itemId}", userId, parameters);
    }

    public ResponseEntity<Object> itemsGetAll(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> itemsGetAllSearch(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text
        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> addComments(Long userId, Long itemId, CommentsDTO commentsDTO) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId
        );
        return post("/{itemId}/comment", userId, parameters);
    }
}
