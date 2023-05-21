package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentReceived;
import ru.practicum.shareit.constants.Headers;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Processing endpoints. Working with Items.
 *
 * @see Item
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Object> addItem(
            @RequestHeader(Headers.USER_ID) Long userId,
            @RequestBody @Valid Item item
    ) {
        return itemService.addItem(item, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader(Headers.USER_ID) Long authorId,
            @PathVariable Long itemId,
            @RequestBody CommentReceived comment
    ) {
        return itemService.addComment(authorId, itemId, comment);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(
            @PathVariable Long id,
            @RequestHeader(Headers.USER_ID) Long userId,
            @RequestBody Item item
    ) {
        return itemService.updateItem(id, item, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(
            @RequestHeader(Headers.USER_ID) Long userId,
            @PathVariable Long id
    ) {
        return itemService.getItemById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(
            @RequestHeader(Headers.USER_ID) Long userId,
            Integer from,
            Integer size
    ) {
        return itemService.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByContent(
            @RequestParam(value = "text", required = false) String content,
            @RequestHeader(Headers.USER_ID) Long userId,
            Integer from,
            Integer size
    ) {
        if (content == null || content.isBlank())
            return ResponseEntity.of(Optional.of(new ArrayList<>()));
        return itemService.getItemByContent(content, userId, from, size);

    }


}
