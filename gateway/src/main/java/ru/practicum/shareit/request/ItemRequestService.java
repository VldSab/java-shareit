package ru.practicum.shareit.request;

import org.springframework.http.ResponseEntity;

public interface ItemRequestService {
    ResponseEntity<Object> addItemRequest(Long requesterId, ItemRequest itemRequest);

    ResponseEntity<Object> getAllRequestersRequests(Long requesterId);

    ResponseEntity<Object> getAllOtherUsersRequests(Long requesterId, Integer from, Integer size);

    ResponseEntity<Object> getById(Long requesterId, Long requestId);
}
