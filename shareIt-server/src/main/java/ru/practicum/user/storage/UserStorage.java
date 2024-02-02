package ru.practicum.user.storage;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;


@Repository
public interface UserStorage extends JpaRepository<User, Long> {
}
