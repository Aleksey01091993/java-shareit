package ru.practicum.shareit.user.storage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;


@Repository
public interface UserStorage extends CrudRepository<User, Long> {

}
