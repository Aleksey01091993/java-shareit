package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRequestStorage extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestor_IdOrderByCreatedAsc(Long id);
    List<ItemRequest> findByDescriptionIgnoreCaseInOrderByCreatedAsc(Collection<String> collection, Pageable pageable);
}
