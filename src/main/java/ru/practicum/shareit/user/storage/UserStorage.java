package ru.practicum.shareit.user.storage;


import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.user.model.User;



public interface UserStorage extends CrudRepository<User, Long> {}
