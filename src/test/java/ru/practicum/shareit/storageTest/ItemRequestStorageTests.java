package ru.practicum.shareit.storageTest;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@DataJpaTest
public class ItemRequestStorageTests {
    @Autowired
    private ItemRequestStorage requestStorage;
    @Autowired
    private UserStorage userStorage;

    @Test
    public void create() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        ItemRequest itemRequest = requestStorage.save(
                new ItemRequest(null, "text", user, LocalDateTime.now(), null)
        );
        assertThat(itemRequest, notNullValue());
        assertThat(itemRequest.getId(), notNullValue());

    }

    @Test
    public void findBy() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        ItemRequest itemRequest = new ItemRequest(null, "text", user, LocalDateTime.now(), null);
        ItemRequest itemRequestSave = requestStorage.save(itemRequest);
        ItemRequest itemRequestGet = requestStorage.findById(itemRequestSave.getId()).orElse(null);

        assertThat(itemRequestGet, equalTo(itemRequestSave));

    }

    @Test
    public void findAll() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        ItemRequest itemRequest = requestStorage.save(
                new ItemRequest(null, "text", user, LocalDateTime.now(), null)
        );
        List<ItemRequest> itemRequests = requestStorage.findAll();
        assertThat(itemRequests, notNullValue());
        assertThat(itemRequests.get(0), equalTo(itemRequest));
    }

    @Test
    public void findByRequestor_IdOrderByCreatedAsc() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        ItemRequest itemRequest = requestStorage.save(
                new ItemRequest(null, "text", user, LocalDateTime.now(), null)
        );
        List<ItemRequest> itemRequests = requestStorage.findByRequestor_IdOrderByCreatedAsc(1L);
        assertThat(itemRequests.get(0), equalTo(itemRequest));
    }

    @Test
    public void findByRequestor_IdOrderByCreatedAscPageable() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        ItemRequest itemRequest = requestStorage.save(
                new ItemRequest(null, "text", user, LocalDateTime.now(), null)
        );
        List<ItemRequest> itemRequests = requestStorage.findByRequestor_IdNotOrderByCreatedAsc(2L, PageRequest.of(0, 1));
        System.out.println(itemRequests);
        assertThat(itemRequests.get(0), equalTo(itemRequest));
    }
}
