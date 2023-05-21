package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(Long requesterId, ItemRequest itemRequest);

    List<ItemRequestInfoDto> getAllRequestersRequests(Long requesterId);

    List<ItemRequestInfoDto> getAllOtherUsersRequests(Long requesterId, Integer from, Integer size);

    ItemRequestInfoDto getById(Long requesterId, Long requestId);
}
