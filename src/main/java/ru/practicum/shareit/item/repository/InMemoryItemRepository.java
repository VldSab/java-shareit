package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {


    @Override
    public Item addItem(Item item) {
        return null;
    }

    @Override
    public Item updateItem(Long id, Item item) {
        return null;
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Item> getUserItems(Long id) {
        return null;
    }

    @Override
    public List<Item> getItemByContent(String content) {
        return null;
    }
}
