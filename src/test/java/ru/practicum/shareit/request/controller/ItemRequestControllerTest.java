package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;

    private static final User myUser = User.builder()
            .id(1L)
            .email("user@gmail.com")
            .name("John")
            .build();
    private static final ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .requester(myUser)
            .description("description")
            .created(LocalDateTime.now().minusDays(1))
            .build();

    @Test
    void addItemRequest_whenInvoked_thenReturnItemRequestDto() {
        ItemRequestDto itemRequestDto = ItemRequestMapper.toDto(itemRequest);
        when(itemRequestService.addItemRequest(myUser.getId(), itemRequest))
                .thenReturn(itemRequestDto);

        assertEquals(itemRequestDto, itemRequestController.addItemRequest(myUser.getId(), itemRequest));
    }

    @Test
    void getAllItemRequests_whenInvoked_thenReturnListOfItemRequestInfoDto() {
        List<ItemRequestInfoDto> itemRequestList = List.of(ItemRequestMapper.toInfoDto(itemRequest, List.of()));
        when(itemRequestService.getAllRequestersRequests(myUser.getId()))
                .thenReturn(itemRequestList);

        assertEquals(itemRequestList,
                itemRequestController.getAllItemRequests(myUser.getId()));
    }

    @Test
    void getAllOfOtherUsers_whenInvoked_thenReturnListOfItemRequestInfoDto() {
        List<ItemRequestInfoDto> itemRequestList = List.of(ItemRequestMapper.toInfoDto(itemRequest, List.of()));
        when(itemRequestService.getAllOtherUsersRequests(myUser.getId(), null, null))
                .thenReturn(itemRequestList);

        assertEquals(itemRequestList,
                itemRequestController.getAllOfOtherUsers(myUser.getId(), null, null));
    }

    @Test
    void getItemRequestById_whenInvoked_thenReturnItemRequestInfoDto() {
        ItemRequestInfoDto itemRequestInfoDto = ItemRequestMapper.toInfoDto(itemRequest, List.of());
        when(itemRequestService.getById(myUser.getId(), itemRequest.getId()))
                .thenReturn(itemRequestInfoDto);

        assertEquals(itemRequestInfoDto,
                itemRequestController.getItemRequestById(myUser.getId(), itemRequest.getId()));
    }
}