package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.user.DTO.UserRequestDTO;

import java.util.Map;
import java.util.function.Supplier;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory((Supplier<ClientHttpRequestFactory>) HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createUser(UserRequestDTO userRequestDTO) {
        return post("", userRequestDTO);
    }

    public ResponseEntity<Object> updateUser(Long userId, UserRequestDTO userRequestDTO) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );
        return patch("/{userId}", userId, parameters, userRequestDTO);
    }

    public ResponseEntity<Object> getUser(Long userId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );
        return get("/{userId}", userId, parameters);
    }

    public ResponseEntity<Object> deleteUser(Long userId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );
        return delete("/{userId}", userId, parameters);
    }

    public ResponseEntity<Object> getAllUser() {
        return get("");
    }
}
