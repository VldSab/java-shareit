package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Headers;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentReceived;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    public ItemDto addItem(@RequestHeader(Headers.USER_ID) Long userId, @RequestBody @Valid Item item) {
        return itemService.addItem(item, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestHeader(Headers.USER_ID) Long authorId,
            @PathVariable Long itemId,
            @RequestBody CommentReceived comment) {
        return itemService.addComment(authorId, itemId, comment);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable Long id,
                              @RequestHeader(Headers.USER_ID) Long userId,
                              @RequestBody Item item) {
        return itemService.updateItem(id, item, userId);
    }

    @GetMapping("/{id}")
    public ItemInfoDto getItemById(@RequestHeader(Headers.USER_ID) Long userId, @PathVariable Long id) {
        return itemService.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemInfoDto> getUserItems(
            @RequestHeader(Headers.USER_ID) Long userId,
            Integer from,
            Integer size
    ) {
        return itemService.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByContent(@RequestHeader(Headers.USER_ID) Long userId,
                                           @RequestParam(value = "text", required = false) String content,
                                           Integer from,
                                           Integer size
    ) {
        if (content == null || content.isBlank())
            return new ArrayList<>();
        return itemService.getItemByContent(content, userId, from, size);

    }


}
