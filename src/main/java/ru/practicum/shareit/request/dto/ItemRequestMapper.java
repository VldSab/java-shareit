package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;

/**
 * Mapping ItemRequest to ItemRequestDto
 *
 * @see ItemRequest
 * @see ItemRequestDto
 */
public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }
}
