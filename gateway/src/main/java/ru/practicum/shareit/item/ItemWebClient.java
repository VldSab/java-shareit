package ru.practicum.shareit.item;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentReceived;
import ru.practicum.shareit.constants.Headers;


import javax.validation.Valid;

@FeignClient(value = "item", url = "${shareit-server.url}/items", decode404 = true)
public interface ItemWebClient {
    @PostMapping
    ResponseEntity<Object> addItem(
            @RequestHeader(Headers.USER_ID) Long userId,
            @RequestBody @Valid Item item
    );

    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> addComment(
            @RequestHeader(Headers.USER_ID) Long authorId,
            @PathVariable Long itemId,
            @RequestBody CommentReceived comment
    );

    @PatchMapping("/{id}")
    ResponseEntity<Object> updateItem(
            @PathVariable Long id,
            @RequestHeader(Headers.USER_ID) Long userId,
            @RequestBody Item item
    );

    @GetMapping("/{id}")
    ResponseEntity<Object> getItemById(
            @RequestHeader(Headers.USER_ID) Long userId,
            @PathVariable Long id
    );

    @GetMapping
    ResponseEntity<Object> getUserItems(
            @RequestHeader(Headers.USER_ID) Long userId,
            @RequestParam Integer from,
            @RequestParam Integer size
    );

    @GetMapping("/search")
    ResponseEntity<Object> getItemsByContent(
            @RequestParam(value = "text", required = false) String content,
            @RequestHeader(Headers.USER_ID) Long userId,
            @RequestParam Integer from,
            @RequestParam Integer size
    );
}
