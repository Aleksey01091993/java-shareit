package ru.practicum.shareit.storageTest;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@DataJpaTest
public class ItemStorageTests {

    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private ItemRequestStorage requestStorage;


    @Test
    public void saveItem() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        Item item = new Item(
                null,
                "name",
                "text",
                true,
                null,
                null,
                user,
                null,
                null
        );
        itemStorage.save(item);
        assertThat(item, notNullValue());

    }

    @Test
    public void updateItem() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        Item item = new Item(
                null,
                "name",
                "text",
                true,
                null,
                null,
                user,
                null,
                null
        );
        itemStorage.save(item);
        assertThat(item, notNullValue());
        assertThat(item.getName(), equalTo("name"));
        item.setName("newName");
        itemStorage.save(item);
        assertThat(item.getName(), equalTo("newName"));
    }

    @Test
    public void getItem() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        Item item = new Item(
                null,
                "name",
                "text",
                true,
                null,
                null,
                user,
                null,
                null
        );
        Item itemOne = itemStorage.save(item);
        assertThat(itemOne, notNullValue());
        Item itemTwo = itemStorage.getReferenceById(itemOne.getId());
        assertThat(itemOne, equalTo(itemTwo));
    }

    @Test
    public void getAllItem() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        Item item = new Item(
                null,
                "name",
                "text",
                true,
                null,
                null,
                user,
                null,
                null
        );
        Item itemOne = new Item(
                null,
                "name",
                "text",
                true,
                null,
                null,
                user,
                null,
                null
        );
        Item itemTwo = new Item(
                null,
                "name",
                "text",
                true,
                null,
                null,
                user,
                null,
                null
        );

        itemStorage.save(item);
        itemStorage.save(itemOne);
        itemStorage.save(itemTwo);
        List<Item> itemList = itemStorage.findAll();
        assertThat(itemList, notNullValue());
        assertThat(itemList.get(0), equalTo(item));
        assertThat(itemList.get(1), equalTo(itemOne));
        assertThat(itemList.get(2), equalTo(itemTwo));
    }

    @Test
    public void findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        Item item = new Item(
                null,
                "name",
                "text",
                true,
                null,
                null,
                user,
                null,
                null
        );
        Item saveItem = itemStorage.save(item);
        List<Item> items =
                itemStorage
                        .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable("text", "text", true);
        assertThat(item, notNullValue());
        assertThat(items.get(0), equalTo(saveItem));
    }

    @Test
    public void findAllByOwnerIdOrderById() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        Item item = new Item(
                null,
                "name",
                "text",
                true,
                null,
                null,
                user,
                null,
                null
        );
        Item saveItem = itemStorage.save(item);
        assertThat(saveItem, notNullValue());
        List<Item> items = itemStorage.findAllByOwnerIdOrderById(1L);
        assertThat(item, notNullValue());

    }
    @Test
    public void findAllByOwnerIdOrderByIdPage() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        Item item = new Item(
                null,
                "name",
                "text",
                true,
                null,
                null,
                user,
                null,
                null
        );
        Item saveItem = itemStorage.save(item);
        List<Item> itemPageable = itemStorage.findAllByOwnerIdOrderById(1L, PageRequest.of(0, 1));
        assertThat(itemPageable, notNullValue());
    }


    @Test
    public void findByRequest_Id() {
        User user = userStorage
                .save(new User(null, "name", "email@email.ru"));
        ItemRequest itemRequest = requestStorage.save(new ItemRequest(
                null,
                "text",
                user,
                LocalDateTime.now(),
                null
        ));
        Item item = new Item(
                null,
                "name",
                "text",
                true,
                null,
                null,
                user,
                itemRequest,
                null
        );
        Item item1 = itemStorage.save(item);
        List<Item> items = itemStorage.findByRequest_Id(1L);
        assertThat(item, notNullValue());
        assertThat(items.get(0), equalTo(item1));
    }


}
