package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
public class UserService {

    @Autowired
    private UserStorage storage;



    public User create(User user) {
        return storage.save(user);
    }
}
