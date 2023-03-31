package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

/**
 * Mapping Item to ItemDto
 * @see Item
 * @see ItemDto
 */
public class ItemMapper {
    public static ItemDto mapToDTO(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.isAvailable())
                .build();
    }
}