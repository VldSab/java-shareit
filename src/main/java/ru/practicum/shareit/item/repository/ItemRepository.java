package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item addItem(Item item);

    Item updateItem(Long id, Item item);

    Optional<Item> getItemById(Long id);

    List<Item> getUserItems(Long id);

    List<Item> getItemByContent(String content);
}
