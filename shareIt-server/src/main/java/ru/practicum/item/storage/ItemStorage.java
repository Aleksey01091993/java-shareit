package ru.practicum.item.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.item.model.Item;

import java.util.List;


@Repository
public interface ItemStorage extends JpaRepository<Item, Long> {
    List<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(String search, String search1, Boolean in);
    List<Item> findAllByOwnerIdOrderById(Long id);
    List<Item> findByRequest_Id(Long id);
    List<Item> findAllByOwnerIdOrderById(Long userId, Pageable pageable);

}
