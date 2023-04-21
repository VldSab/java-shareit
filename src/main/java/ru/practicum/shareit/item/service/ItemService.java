package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentReceived;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Item item, Long userId);

    ItemDto updateItem(Long id, Item item, Long userId);

    ItemInfoDto getItemById(Long id, Long userId);

    List<ItemInfoDto> getUserItems(Long userId);

    List<ItemDto> getItemByContent(String content, Long userId);

    CommentDto addComment(Long authorId, Long itemId, CommentReceived comment);

}
