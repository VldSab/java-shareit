package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Headers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
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

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable Long id,
                              @RequestHeader(Headers.USER_ID) Long userId,
                              @RequestBody Item item) {
        return itemService.updateItem(id, item, userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@RequestHeader(Headers.USER_ID) Long userId, @PathVariable Long id) {
        return  itemService.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(Headers.USER_ID) Long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByContent(@RequestHeader(Headers.USER_ID) Long userId,
                                           @RequestParam(value = "text", required = false) String content) {
        if (content == null || content.isBlank())
            return new ArrayList<>();
        return itemService.getItemByContent(content, userId);

    }



}
