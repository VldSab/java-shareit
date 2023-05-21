package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.comment.CommentReceived;


public interface ItemService {
    ResponseEntity<Object> addItem(Item item, Long userId);

    ResponseEntity<Object> updateItem(Long id, Item item, Long userId);

    ResponseEntity<Object> getItemById(Long id, Long userId);

    ResponseEntity<Object> getUserItems(Long userId, Integer from, Integer size);

    ResponseEntity<Object> getItemByContent(String content, Long userId, Integer from, Integer size);

    ResponseEntity<Object> addComment(Long authorId, Long itemId, CommentReceived comment);

}
