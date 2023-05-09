package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceStandardTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceStandard itemRequestService;

    private static final User myUser = User.builder()
            .id(1L)
            .email("user@gmail.com")
            .name("John")
            .build();

    private static final User otherUser = User.builder()
            .id(2L)
            .email("other@gmail.com")
            .name("Frank")
            .build();

    private static final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .owner(myUser)
            .description("Simple item")
            .isAvailable(true)
            .build();

    private static final ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .requester(null)
            .description("description")
            .created(LocalDateTime.now())
            .build();

    @Test
    void addItemRequest_whenRequesterExists_thenReturnItemRequestDto() {
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.of(otherUser));
        ItemRequest expectedItem = ItemRequest.builder()
                .id(itemRequest.getId())
                .requester(otherUser)
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
        when(itemRequestRepository.save(expectedItem)).thenReturn(expectedItem);

        ItemRequestDto result = itemRequestService.addItemRequest(otherUser.getId(), itemRequest);

        assertEquals(ItemRequestMapper.toDto(expectedItem), result);
        verify(itemRequestRepository).save(expectedItem);
    }

    @Test
    void getAllRequestersRequests() {
    }

    @Test
    void getAllOtherUsersRequests() {
    }

    @Test
    void getById() {
    }
}