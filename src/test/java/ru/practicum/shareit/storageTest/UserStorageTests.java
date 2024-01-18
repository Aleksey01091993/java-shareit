package ru.practicum.shareit.storageTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@DataJpaTest
public class UserStorageTests {

    @Autowired
    private UserStorage userStorage;

    @Test
    public void saveUser() {
        UserResponseDTO responseDTO = new UserResponseDTO("name", "email.email.ru");
        User user = userStorage.save(new User(null, "name", "email.email.ru"));
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(responseDTO.getName()));
        assertThat(user.getEmail(), equalTo(responseDTO.getEmail()));

    }
    @Test
    public void updateUser() {
        User user = userStorage.save(new User(null, "name", "email.email.ru"));
        User userNew = userStorage.save(new User(1L, "name", "emailNew.email.ru"));
        assertThat(userNew.getId(), notNullValue());
        assertThat(userNew.getName(), equalTo(user.getName()));
        assertThat(userNew.getEmail(), equalTo("emailNew.email.ru"));

    }

    @Test
    public void getUser() {
        User user = userStorage.save(new User(null, "name", "email.email.ru"));
        User userGet = userStorage.findById(user.getId()).orElse(null);
        assertThat(userGet, notNullValue());
        assertThat(user, equalTo(userGet));
    }

    @Test
    public void deleteUser() {
        User user = userStorage
                .save(new User(null, "name", "email.email.ru"));
        userStorage.deleteById(user.getId());
        User userGet = userStorage.findById(user.getId()).orElse(null);
        assertThat(user, notNullValue());
        assertThat(userGet, nullValue());

    }

    @Test
    public void gatAllUsers() {
        User user = userStorage
                .save(new User(null, "name", "email.email.ru"));
        User userOne = userStorage
                .save(new User(null, "name", "emailOne.email.ru"));
        User userTwo = userStorage
                .save(new User(null, "name", "emailTwo.email.ru"));
        List<User> userList = userStorage.findAll();
        assertThat(userList.size(), equalTo(3));
        assertThat(userList, notNullValue());
        assertThat(userList.get(0), equalTo(user));
        assertThat(userList.get(1), equalTo(userOne));
        assertThat(userList.get(2), equalTo(userTwo));
    }
}
