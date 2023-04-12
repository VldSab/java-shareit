package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of ItemRepository.
 *
 * @see ItemRepository
 */
@Repository
public class InMemoryItemRepository implements ItemRepository {
    private static final Map<Long, Item> items = new HashMap<>();
    private static Long count = 1L;

    @Override
    public Item addItem(Item item) {
        item.setId(count);
        items.put(count, item);
        return items.get(count++);
    }

    @Override
    public Item updateItem(Long id, Item item) {
        Item cur = items.get(id);
        if (item.getOwner() != null)
            cur.setOwner(item.getOwner());
        if (item.getIsAvailable() != null)
            cur.setIsAvailable(item.getIsAvailable());
        if (item.getName() != null)
            cur.setName(item.getName());
        if (item.getDescription() != null)
            cur.setDescription(item.getDescription());
        return cur;
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        Item cur = items.get(id);
        return Optional.of(cur);
    }

    @Override
    public List<Item> getUserItems(Long id) {
        return items.values().stream()
                .filter(it -> Objects.equals(it.getOwner().getId(), id))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemByContent(String content) {
        return items.values().stream()
                .filter(it -> (it.getDescription().toLowerCase().contains(content.toLowerCase())
                        || it.getName().toLowerCase().contains(content.toLowerCase()))
                        && it.getIsAvailable()
                ).collect(Collectors.toList());
    }
}
