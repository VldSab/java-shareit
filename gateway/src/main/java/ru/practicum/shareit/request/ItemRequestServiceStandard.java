package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceStandard implements ItemRequestService {

    private final RequestWebClient requestWebClient;

    @Override
    public ResponseEntity<Object> addItemRequest(Long requesterId, ItemRequest itemRequest) {
        return requestWebClient.addItemRequest(requesterId, itemRequest);
    }

    @Override
    public ResponseEntity<Object> getAllRequestersRequests(Long requesterId) {
        return requestWebClient.getAllItemRequests(requesterId);
    }

    @Override
    public ResponseEntity<Object> getAllOtherUsersRequests(Long requesterId, Integer from, Integer size) {
        return requestWebClient.getAllOfOtherUsers(requesterId, from, size);
    }

    @Override
    public ResponseEntity<Object> getById(Long requesterId, Long requestId) {
        return requestWebClient.getItemRequestById(requesterId, requestId);
    }
}
