package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Headers;


/**
 * Обработка запросов по добавлению requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(
            @RequestHeader(Headers.USER_ID) Long requesterId,
            @RequestBody ItemRequest itemRequest
    ) {
        return itemRequestService.addItemRequest(requesterId, itemRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader(Headers.USER_ID) Long requesterId
    ) {
        return itemRequestService.getAllRequestersRequests(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOfOtherUsers(
            @RequestHeader(Headers.USER_ID) Long requesterId,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size
    ) {
        return itemRequestService.getAllOtherUsersRequests(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader(Headers.USER_ID) Long requesterId,
            @PathVariable Long requestId
    ) {
        return itemRequestService.getById(requesterId, requestId);
    }
}
