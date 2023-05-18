package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Headers;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * Обработка ззапросов по добавлению requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(
            @RequestHeader(Headers.USER_ID) Long requesterId,
            @RequestBody ItemRequest itemRequest
    ) {
        return itemRequestService.addItemRequest(requesterId, itemRequest);
    }

    @GetMapping
    public List<ItemRequestInfoDto> getAllItemRequests(
            @RequestHeader(Headers.USER_ID) Long requesterId
    ) {
        return itemRequestService.getAllRequestersRequests(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestInfoDto> getAllOfOtherUsers(
            @RequestHeader(Headers.USER_ID) Long requesterId,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size
    ) {
        return itemRequestService.getAllOtherUsersRequests(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestInfoDto getItemRequestById(
            @RequestHeader(Headers.USER_ID) Long requesterId,
            @PathVariable Long requestId
    ) {
        return itemRequestService.getById(requesterId, requestId);
    }
}
