package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.comment.CommentReceived;


/**
 * Items service implementation.
 *
 * @see ItemService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceStandard implements ItemService {

    private final ItemWebClient itemWebClient;

    @Override
    public ResponseEntity<Object> addItem(Item item, Long userId) {
        return itemWebClient.addItem(userId, item);
    }

    @Override
    public ResponseEntity<Object> updateItem(Long id, Item item, Long userId) {
        return itemWebClient.updateItem(id, userId, item);
    }


    @Override
    public ResponseEntity<Object> getItemById(Long id, Long userId) {
        return itemWebClient.getItemById(userId, id);
    }

    @Override
    public ResponseEntity<Object> getUserItems(Long userId, Integer from, Integer size) {
        return itemWebClient.getUserItems(userId, from, size);
    }

    @Override
    public ResponseEntity<Object> getItemByContent(String content, Long userId, Integer from, Integer size) {
        return itemWebClient.getItemsByContent(content, userId, from, size);
    }

    @Override
    public ResponseEntity<Object> addComment(Long authorId, Long itemId, CommentReceived commentReceived) {
        return itemWebClient.addComment(authorId, itemId, commentReceived);
    }
}
