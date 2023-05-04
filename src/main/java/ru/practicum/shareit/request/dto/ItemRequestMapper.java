package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Mapping ItemRequest to ItemRequestDto
 *
 * @see ItemRequest
 * @see ItemRequestDto
 */
public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestInfoDto toInfoDto(ItemRequest itemRequest, List<ItemResponse> responses) {
        return ItemRequestInfoDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(responses)
                .build();
    }

    public static ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .requestId(item.getRequestId())
                .itemId(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .build();
    }
}
