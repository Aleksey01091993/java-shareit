package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.exception.NotFound404;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User create(UserDTO user) {
        return storage.save(UserMapper.toUser(user));
    }

    public User update(UserDTO user, Long userId) {
        User newUser = get(userId);
        if (user.getName() != null) newUser.setName(user.getName());
        if (user.getEmail() != null) newUser.setEmail(user.getEmail());
        return storage.save(newUser);

    }

    public User get(Long userId) {
        return storage.findById(userId)
                .orElseThrow(() -> new NotFound404("user not found by id:" + userId));
    }

    public User delete(Long id) {
        if (id == null) {
            throw new EmailException("Пустой запрос на удоление");
        }
        User user = get(id);
        storage.deleteById(id);
        return user;
    }

    public List<User> getAll() {
        return storage.findAll();
    }
}