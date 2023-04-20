package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Item item, Long userId);

    ItemDto updateItem(Long id, Item item, Long userId);

    ItemDto getItemById(Long id, Long userId);

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> getItemByContent(String content, Long userId);

}
