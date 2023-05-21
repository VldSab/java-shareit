package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
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

    private static final ItemRequest otherRequest = ItemRequest.builder()
            .id(2L)
            .requester(null)
            .description("description2")
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
    void addItemRequest_whenRequesterNotExists_thenThrowNotFoundException() {
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.addItemRequest(otherUser.getId(), itemRequest));
    }

    @Test
    void getAllRequestersRequests_whenRequesterExists_thenReturnListOfItemRequestInfoDto() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);
        when(itemRepository.findAllByRequestIdNotNull()).thenReturn(List.of(item));
        when(itemRequestRepository
                .findAllByRequester_IdOrderByCreatedDesc(otherUser.getId())).thenReturn(List.of(itemRequest));

        List<ItemRequestInfoDto> result = itemRequestService.getAllRequestersRequests(otherUser.getId());

        assertInstanceOf(ItemRequestInfoDto.class, result.get(0));
        assertEquals(result.size(), 1);
    }

    @Test
    void getAllRequestersRequests_whenRequesterNotExists_thenThrowNotFoundException() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemRequestService.getAllRequestersRequests(otherUser.getId()));
    }

    @Test
    void getAllOtherUsersRequests_whenRequesterExistsAndNoPagination_thenReturnListOfItemRequestInfoDto() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);
        when(itemRequestRepository.findAllByRequester_IdNotOrderByCreatedDesc(otherUser.getId(), null))
                .thenReturn(new PageImpl<>(List.of(otherRequest)));
        when(itemRepository.findAllByRequestIdNotNull()).thenReturn(List.of(item));

        List<ItemRequestInfoDto> result = itemRequestService
                .getAllOtherUsersRequests(otherUser.getId(), null, null);

        assertEquals(1, result.size());
        assertInstanceOf(ItemRequestInfoDto.class, result.get(0));
    }

    @Test
    void getAllOtherUsersRequests_whenRequesterExistsAndPaginationIsValid_thenReturnListOfItemRequestInfoDto() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);
        Pageable pageable = PageRequest.of(0, 10);
        when(itemRequestRepository.findAllByRequester_IdNotOrderByCreatedDesc(otherUser.getId(), pageable))
                .thenReturn(new PageImpl<>(List.of(otherRequest)));
        when(itemRepository.findAllByRequestIdNotNull()).thenReturn(List.of(item));

        List<ItemRequestInfoDto> result = itemRequestService
                .getAllOtherUsersRequests(otherUser.getId(), 0, 10);

        assertEquals(1, result.size());
        assertInstanceOf(ItemRequestInfoDto.class, result.get(0));
    }

    @Test
    void getAllOtherUsersRequests_whenRequesterNotExists_thenThrowNotFoundException() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemRequestService
                .getAllOtherUsersRequests(otherUser.getId(), 0, 10));
    }

    @Test
    void getAllOtherUsersRequests_whenPaginationIsNotValid_thenThrowValidationException() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);

        assertThrows(ValidationException.class, () -> itemRequestService
                .getAllOtherUsersRequests(otherUser.getId(), -1, 1));
        assertThrows(ValidationException.class, () -> itemRequestService
                .getAllOtherUsersRequests(otherUser.getId(), 1, -1));
        assertThrows(ValidationException.class, () -> itemRequestService
                .getAllOtherUsersRequests(otherUser.getId(), 0, 0));
    }

    @Test
    void getById_whenRequesterExistsAndRequestExists_thenReturnItemRequestInfoDto() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(itemRequest.getId())).thenReturn(List.of(item));

        ItemRequestInfoDto result = itemRequestService.getById(otherUser.getId(), itemRequest.getId());

        assertEquals(1, result.getItems().size());
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(ItemRequestMapper.toResponse(item), result.getItems().get(0));
    }

    @Test
    void getById_whenRequesterNotExists_thenThrowNotFoundException() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> itemRequestService.getById(otherUser.getId(), itemRequest.getId()));
    }

    @Test
    void getById_whenRequestNotExists_thenThrowNotFoundException() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemRequestService.getById(otherUser.getId(), itemRequest.getId()));
    }
}