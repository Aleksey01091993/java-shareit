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
    private final Set<String> email = new HashSet<>();

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User create(UserDTO user) {
        if (this.email.contains(user.getEmail())) {
            throw new EmailException("пользователь с таким email уже существует");
        }
        this.email.add(user.getEmail());
        return storage.save(UserMapper.toUser(user));
    }

    public User update(UserDTO user, Long userId) {
        User newUser = get(userId);
        if (user.getEmail() == null && user.getName() != null) {
            newUser.setName(user.getName());
            return storage.save(newUser);
        } else if (user.getEmail() != null && user.getName() == null) {
            if (user.getEmail().equals(newUser.getEmail())) {
                return newUser;
            }
            if (this.email.contains(user.getEmail())) {
                throw new EmailException("пользователь с таким email уже существует");
            }
            this.email.remove(newUser.getEmail());
            this.email.add(user.getEmail());
            newUser.setEmail(user.getEmail());
            return storage.save(newUser);
        } else {
            if (this.email.contains(user.getEmail())) {
                throw new EmailException("пользователь с таким email уже существует");
            }
            this.email.remove(get(userId).getEmail());
            this.email.add(user.getEmail());
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            return storage.save(newUser);
        }

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
        email.remove(user.getEmail());
        storage.deleteById(id);
        return user;
    }

    public List<User> getAll() {
        return storage.findAll();
    }
}
