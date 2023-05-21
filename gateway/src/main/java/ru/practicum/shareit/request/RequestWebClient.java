package ru.practicum.shareit.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Headers;

@FeignClient(value = "request", url = "${shareit-server.url}/requests", decode404 = true)
public interface RequestWebClient {
    @PostMapping
    ResponseEntity<Object> addItemRequest(
            @RequestHeader(Headers.USER_ID) Long requesterId,
            @RequestBody ItemRequest itemRequest
    );

    @GetMapping
    ResponseEntity<Object> getAllItemRequests(
            @RequestHeader(Headers.USER_ID) Long requesterId
    );

    @GetMapping("/all")
    ResponseEntity<Object> getAllOfOtherUsers(
            @RequestHeader(Headers.USER_ID) Long requesterId,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size
    );

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getItemRequestById(
            @RequestHeader(Headers.USER_ID) Long requesterId,
            @PathVariable Long requestId
    );
}
