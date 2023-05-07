package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceStandardTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemServiceStandard itemService;

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

    private static final Comment comment = Comment.builder()
            .id(1L)
            .item(item)
            .created(LocalDateTime.now())
            .author(otherUser)
            .text("Good one")
            .build();

    private static final Booking booking1 = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().minusDays(3))
            .end(LocalDateTime.now().minusDays(2))
            .item(item)
            .booker(otherUser)
            .build();

    @Test
    void addItem_whenUserExists_thenReturnItemDto() {
        when(userRepository.findById(myUser.getId()))
                .thenReturn(Optional.of(myUser));
        when(itemRepository.save(item))
                .thenReturn(item);

        ItemDto itemDto = itemService.addItem(item, myUser.getId());

        assertEquals(itemDto, ItemMapper.toDto(item));
        verify(itemRepository).save(item);
    }

    @Test
    void addItem_whenUserNotExists_thenThrowNotFoundException() {
        when(userRepository.findById(myUser.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.addItem(item, myUser.getId()));
    }

    @Test
    void updateItem_whenUserFoundAndItemFoundAndUserIsOwner_thenReturnItemDto() {
        when(userRepository.findById(myUser.getId()))
                .thenReturn(Optional.of(myUser));
        when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.of(item));
        Item currentItem = Item.builder()
                .name("Awesome item")
                .build();
        currentItem.setId(item.getId());
        currentItem.setOwner(myUser);
        currentItem.setIsAvailable(true);
        currentItem.setDescription(item.getDescription());
        when(itemRepository.save(currentItem))
                .thenReturn(currentItem);
        ItemDto currentItemDto = ItemMapper.toDto(currentItem);

        ItemDto itemDto = itemService.updateItem(item.getId(), currentItem, myUser.getId());

        assertEquals(currentItemDto, itemDto);
    }

    @Test
    void updateItem_whenUserNotFound_thenThrowNotFoundException() {
        when(userRepository.findById(myUser.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.updateItem(item.getId(), item, myUser.getId()));
    }

    @Test
    void updateItem_whenItemNotFound_thenThrowNotFoundException() {
        when(userRepository.findById(myUser.getId()))
                .thenReturn(Optional.of(myUser));
        when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> itemService.updateItem(item.getId(), item, myUser.getId()));
    }

    @Test
    void getItemById_whenUserFoundAndItemFound_thenReturnItemDto() {
        when(userRepository.existsById(myUser.getId())).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository
                .findByItem_IdAndItem_Owner_IdAndStatusNotOrderByStartAsc(
                        item.getId(), myUser.getId(), BookingStatus.REJECTED
                )
        ).thenReturn(List.of(booking1));
        when(commentRepository.findAllByItem_IdOrderByCreated(item.getId()))
                .thenReturn(List.of(comment));

        ItemInfoDto itemInfoDto = itemService.getItemById(item.getId(), myUser.getId());
        CommentDto commentDto = CommentMapper.toDto(comment);

        assertEquals(itemInfoDto, ItemMapper.toInfoDto(item, List.of(commentDto), booking1, null));
    }

    @Test
    void getItemById_whenUserNotFoundAndItemFound_thenThrowNotFoundException() {
        when(userRepository.existsById(myUser.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemService.getItemById(item.getId(), myUser.getId()));
    }

    @Test
    void getItemById_whenUserFoundAndItemNotFound_thenThrowNotFoundException() {
        when(userRepository.existsById(myUser.getId())).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItemById(item.getId(), myUser.getId()));
    }

    @Test
    void getItemByContent() {
    }

    @Test
    void addComment() {
    }
}