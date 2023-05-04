package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where lower(i.name) like lower(concat('%', ?1, '%')) " +
            "or lower(i.description) like lower(concat('%', ?1, '%'))" +
            "and i.isAvailable is true")
    List<Item> findByContent(String content);

    List<Item> findAllByOwner_IdOrderById(Long ownerId);
    List<Item> findAllByRequestIdNotNull();
    List<Item> findAllByRequestId(Long requestId);
}
