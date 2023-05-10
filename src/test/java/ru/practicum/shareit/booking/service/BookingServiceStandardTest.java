package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ObjectUnavailableException;
import ru.practicum.shareit.exceptions.UnsupportedBookingStatusException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceStandardTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BookingServiceStandard bookingServiceStandard;

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

    private static final Booking booking1 = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().plusDays(2))
            .end(LocalDateTime.now().plusDays(3))
            .item(item)
            .booker(otherUser)
            .status(BookingStatus.WAITING)
            .build();

    private static final Booking bookingFuture = Booking.builder()
            .id(booking1.getId())
            .start(booking1.getStart())
            .end(booking1.getEnd())
            .booker(booking1.getBooker())
            .item(booking1.getItem())
            .status(BookingStatus.FUTURE)
            .build();
    private static final Booking bookingPast = Booking.builder()
            .id(booking1.getId())
            .start(booking1.getStart())
            .end(booking1.getEnd())
            .booker(booking1.getBooker())
            .item(booking1.getItem())
            .status(BookingStatus.PAST)
            .build();
    private static final Booking bookingCurrent = Booking.builder()
            .id(booking1.getId())
            .start(booking1.getStart())
            .end(booking1.getEnd())
            .booker(booking1.getBooker())
            .item(booking1.getItem())
            .status(BookingStatus.CURRENT)
            .build();
    private static final Booking bookingApproved = Booking.builder()
            .id(booking1.getId())
            .start(booking1.getStart())
            .end(booking1.getEnd())
            .booker(booking1.getBooker())
            .item(booking1.getItem())
            .status(BookingStatus.APPROVED)
            .build();

    @Test
    void save_whenUserExistsAndItemExistsAndItemIsAvailableAndUserIsNotAnOwnerAndDateIsValid_thenReturnBookingDto() {
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.of(otherUser));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(booking1)).thenReturn(booking1);
        BookingDto expectedBookingDto = BookingMapper.toDto(booking1);
        ExternalBookingDto externalBookingDto = ExternalBookingDto.builder()
                .id(booking1.getId())
                .start(booking1.getStart())
                .end(booking1.getEnd())
                .itemId(item.getId())
                .bookerId(booking1.getBooker().getId())
                .status(booking1.getStatus().name())
                .build();

        BookingDto result = bookingServiceStandard.save(otherUser.getId(), externalBookingDto);

        assertEquals(expectedBookingDto, result);
        verify(userRepository).findById(anyLong());
        verify(itemRepository).findById(anyLong());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void save_whenUserNotExists_thenThrowNotFoundException() {
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.empty());
        ExternalBookingDto externalBookingDto = ExternalBookingDto.builder()
                .id(booking1.getId())
                .start(booking1.getStart())
                .end(booking1.getEnd())
                .itemId(item.getId())
                .bookerId(booking1.getBooker().getId())
                .status(booking1.getStatus().name())
                .build();

        assertThrows(NotFoundException.class, () -> bookingServiceStandard.save(otherUser.getId(), externalBookingDto));
    }

    @Test
    void save_whenItemNotExists_thenThrowNotFoundException() {
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.of(otherUser));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());
        ExternalBookingDto externalBookingDto = ExternalBookingDto.builder()
                .id(booking1.getId())
                .start(booking1.getStart())
                .end(booking1.getEnd())
                .itemId(item.getId())
                .bookerId(booking1.getBooker().getId())
                .status(booking1.getStatus().name())
                .build();

        assertThrows(NotFoundException.class, () -> bookingServiceStandard.save(otherUser.getId(), externalBookingDto));
    }

    @Test
    void save_whenItemIsNotAvailable_thenObjectUnavailableException() {
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.of(otherUser));
        Item testItem = Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .isAvailable(false)
                .build();
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(testItem));
        ExternalBookingDto externalBookingDto = ExternalBookingDto.builder()
                .id(booking1.getId())
                .start(booking1.getStart())
                .end(booking1.getEnd())
                .itemId(item.getId())
                .bookerId(booking1.getBooker().getId())
                .status(booking1.getStatus().name())
                .build();

        assertThrows(ObjectUnavailableException.class, () -> bookingServiceStandard.save(otherUser.getId(), externalBookingDto));
    }

    @Test
    void save_whenDateTimeIsNotValid_thenThrowValidationException() {
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.of(otherUser));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        ExternalBookingDto externalBookingDto1 = ExternalBookingDto.builder()
                .id(booking1.getId())
                .start(LocalDateTime.now().minusDays(1))
                .end(booking1.getEnd())
                .itemId(item.getId())
                .bookerId(booking1.getBooker().getId())
                .status(booking1.getStatus().name())
                .build();
        ExternalBookingDto externalBookingDto2 = ExternalBookingDto.builder()
                .id(booking1.getId())
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .itemId(item.getId())
                .bookerId(booking1.getBooker().getId())
                .status(booking1.getStatus().name())
                .build();
        ExternalBookingDto externalBookingDto3 = ExternalBookingDto.builder()
                .id(booking1.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().minusDays(1))
                .itemId(item.getId())
                .bookerId(booking1.getBooker().getId())
                .status(booking1.getStatus().name())
                .build();

        assertThrows(ValidationException.class,
                () -> bookingServiceStandard.save(otherUser.getId(), externalBookingDto1));
        assertThrows(ValidationException.class,
                () -> bookingServiceStandard.save(otherUser.getId(), externalBookingDto2));
        assertThrows(ValidationException.class,
                () -> bookingServiceStandard.save(otherUser.getId(), externalBookingDto3));
    }

    @Test
    void approve_whenBookingExistsAndOwnerExistsAndUserIsOwnerAndStatusIsWaiting_thenReturnBookingDto() {
        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.of(booking1));
        when(userRepository.existsById(myUser.getId())).thenReturn(true);
        when(bookingRepository.save(booking1)).thenReturn(booking1);
        boolean approved = true;
        BookingDto expected = BookingDto.builder()
                .id(booking1.getId())
                .start(booking1.getStart())
                .end(booking1.getEnd())
                .status(BookingStatus.APPROVED)
                .booker(UserMapper.toDto(booking1.getBooker()))
                .item(ItemMapper.toDto(booking1.getItem()))
                .build();

        BookingDto result = bookingServiceStandard.approve(myUser.getId(), booking1.getId(), approved);

        assertEquals(expected, result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void approve_whenBookingNotExists_thenThrowNotFoundException() {
        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingServiceStandard.approve(myUser.getId(), booking1.getId(), true));
    }

    @Test
    void approve_whenUserNotExists_thenThrowNotFoundException() {
        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.of(booking1));
        when(userRepository.existsById(myUser.getId())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingServiceStandard.approve(myUser.getId(), booking1.getId(), true));
    }

    @Test
    void approve_whenUserIsNotNotOwner_thenThrowNotFoundException() {
        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.of(booking1));
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);

        assertThrows(NotFoundException.class,
                () -> bookingServiceStandard.approve(otherUser.getId(), booking1.getId(), true));
    }

    @Test
    void approve_whenStatusIsNotWaiting_thenThrowValidationException() {
        Booking testBooking = Booking.builder()
                .id(booking1.getId())
                .start(booking1.getStart())
                .end(booking1.getEnd())
                .booker(booking1.getBooker())
                .item(booking1.getItem())
                .status(BookingStatus.APPROVED)
                .build();
        when(bookingRepository.findById(testBooking.getId())).thenReturn(Optional.of(testBooking));
        when(userRepository.existsById(myUser.getId())).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> bookingServiceStandard.approve(myUser.getId(), testBooking.getId(), true));
    }

    @Test
    void getBookingById_whenBookingIsExistsAndUserIsBookerOrItemOwner_thenReturnBookingDto() {
        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.of(booking1));
        BookingDto expected = BookingMapper.toDto(booking1);

        BookingDto result1 = bookingServiceStandard.getBookingById(myUser.getId(), booking1.getId());
        BookingDto result2 = bookingServiceStandard.getBookingById(otherUser.getId(), booking1.getId());

        assertEquals(expected, result1);
        assertEquals(expected, result2);
    }

    @Test
    void getBookingById_whenBookingIsNotExists_thenNotFoundException() {
        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingServiceStandard.getBookingById(myUser.getId(), booking1.getId()));
    }

    @Test
    void getBookingById_whenUserIsNotItemOwnerAndNotBookingCreator_thenNotFoundException() {
        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.of(booking1));
        Long newId = 3L;

        assertThrows(NotFoundException.class,
                () -> bookingServiceStandard.getBookingById(newId, booking1.getId()));
    }

    @Test
    void getBookingsByBookerId_whenBookerExistsAndStateIsValidAndNoPagination_thenReturnListOfBookingDto() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);
        when(bookingRepository.findBookingsByBooker_IdOrderByStartDesc(otherUser.getId()))
                .thenReturn(List.of(booking1));
        when(bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(
                eq(otherUser.getId()),
                any(LocalDateTime.class))
        ).thenReturn(List.of(bookingFuture));
        when(bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(
                eq(otherUser.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(List.of(bookingPast));
        when(bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                eq(otherUser.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(List.of(bookingCurrent));
        when(bookingRepository.findBookingsByBooker_IdAndStatusOrderByStartDesc(
                otherUser.getId(),
                BookingStatus.APPROVED)
        ).thenReturn(List.of(bookingApproved));
        List<BookingDto> expectedDtoAll = List.of(BookingMapper.toDto(booking1));
        List<BookingDto> expectedDtoFuture = List.of(BookingMapper.toDto(bookingFuture));
        List<BookingDto> expectedDtoPast = List.of(BookingMapper.toDto(bookingPast));
        List<BookingDto> expectedDtoCurrent = List.of(BookingMapper.toDto(bookingCurrent));
        List<BookingDto> expectedDtoDefault = List.of(BookingMapper.toDto(bookingApproved));

        List<BookingDto> dtoAll = bookingServiceStandard
                .getBookingsByBookerId(otherUser.getId(), "ALL", null, null);
        List<BookingDto> dtoFuture = bookingServiceStandard
                .getBookingsByBookerId(otherUser.getId(), "FUTURE", null, null);
        List<BookingDto> dtoPast = bookingServiceStandard
                .getBookingsByBookerId(otherUser.getId(), "PAST", null, null);
        List<BookingDto> dtoCurrent = bookingServiceStandard
                .getBookingsByBookerId(otherUser.getId(), "CURRENT", null, null);
        List<BookingDto> dtoDefault = bookingServiceStandard
                .getBookingsByBookerId(otherUser.getId(), "APPROVED", null, null);

        assertEquals(expectedDtoAll, dtoAll);
        assertEquals(expectedDtoFuture, dtoFuture);
        assertEquals(expectedDtoPast, dtoPast);
        assertEquals(expectedDtoCurrent, dtoCurrent);
        assertEquals(expectedDtoDefault, dtoDefault);
    }

    @Test
    void getBookingsByBookerId_whenBookerExistsAndStateIsValidAndWithPagination_thenReturnListOfBookingDto() {
        Integer from = 0;
        Integer size = 10;
        Pageable pagination = PageRequest.of(from / size, size);
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);
        when(bookingRepository.findBookingsByBooker_IdOrderByStartDesc(otherUser.getId(), pagination))
                .thenReturn(new PageImpl<>(List.of(booking1)));
        when(bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(
                eq(otherUser.getId()),
                any(LocalDateTime.class),
                eq(pagination))
        ).thenReturn(new PageImpl<>(List.of(bookingFuture)));
        when(bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(
                eq(otherUser.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(pagination))
        ).thenReturn(new PageImpl<>(List.of(bookingPast)));
        when(bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                eq(otherUser.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(pagination))
        ).thenReturn(new PageImpl<>(List.of(bookingCurrent)));
        when(bookingRepository.findBookingsByBooker_IdAndStatusOrderByStartDesc(
                otherUser.getId(),
                BookingStatus.APPROVED,
                pagination)
        ).thenReturn(new PageImpl<>(List.of(bookingApproved)));
        List<BookingDto> expectedDtoAll = List.of(BookingMapper.toDto(booking1));
        List<BookingDto> expectedDtoFuture = List.of(BookingMapper.toDto(bookingFuture));
        List<BookingDto> expectedDtoPast = List.of(BookingMapper.toDto(bookingPast));
        List<BookingDto> expectedDtoCurrent = List.of(BookingMapper.toDto(bookingCurrent));
        List<BookingDto> expectedDtoDefault = List.of(BookingMapper.toDto(bookingApproved));

        List<BookingDto> dtoAll = bookingServiceStandard
                .getBookingsByBookerId(otherUser.getId(), "ALL", from, size);
        List<BookingDto> dtoFuture = bookingServiceStandard
                .getBookingsByBookerId(otherUser.getId(), "FUTURE", from, size);
        List<BookingDto> dtoPast = bookingServiceStandard
                .getBookingsByBookerId(otherUser.getId(), "PAST", from, size);
        List<BookingDto> dtoCurrent = bookingServiceStandard
                .getBookingsByBookerId(otherUser.getId(), "CURRENT", from, size);
        List<BookingDto> dtoDefault = bookingServiceStandard
                .getBookingsByBookerId(otherUser.getId(), "APPROVED", from, size);

        assertEquals(expectedDtoAll, dtoAll);
        assertEquals(expectedDtoFuture, dtoFuture);
        assertEquals(expectedDtoPast, dtoPast);
        assertEquals(expectedDtoCurrent, dtoCurrent);
        assertEquals(expectedDtoDefault, dtoDefault);
    }

    @Test
    void getBookingsByBookerId_whenBookerNotExists_thenThrowNotFoundException() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingServiceStandard.getBookingsByBookerId(
                        otherUser.getId(),
                        "ALL",
                        null,
                        null
                )
        );
    }

    @Test
    void getBookingsByBookerId_whenStateIsUnsupported_thenThrowUnsupportedBookingStatusException() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);

        assertThrows(UnsupportedBookingStatusException.class,
                () -> bookingServiceStandard.getBookingsByBookerId(
                        otherUser.getId(),
                        "UNSUPPORTED_STATE",
                        null,
                        null
                )
        );
    }

    @Test
    void getBookingsByBookerId_whenPaginationIsNotValid_thenThrowValidationException() {
        when(userRepository.existsById(otherUser.getId())).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> bookingServiceStandard.getBookingsByBookerId(
                        otherUser.getId(),
                        "ALL",
                        -1,
                        1
                )
        );
        assertThrows(ValidationException.class,
                () -> bookingServiceStandard.getBookingsByBookerId(
                        otherUser.getId(),
                        "ALL",
                        1,
                        -1
                )
        );
        assertThrows(ValidationException.class,
                () -> bookingServiceStandard.getBookingsByBookerId(
                        otherUser.getId(),
                        "ALL",
                        1,
                        0
                )
        );
    }

    @Test
    void getBookingsByOwner_whenOwnerExistsAndStateIsValidAndNoPagination_thenReturnListOfBookingDto() {
        when(userRepository.existsById(myUser.getId())).thenReturn(true);
        when(bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(myUser.getId()))
                .thenReturn(List.of(booking1));
        when(bookingRepository.findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(
                eq(myUser.getId()),
                any(LocalDateTime.class))
        ).thenReturn(List.of(bookingFuture));
        when(bookingRepository.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(
                eq(myUser.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(List.of(bookingPast));
        when(bookingRepository.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                eq(myUser.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(List.of(bookingCurrent));
        when(bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(
                myUser.getId(),
                BookingStatus.APPROVED)
        ).thenReturn(List.of(bookingApproved));
        List<BookingDto> expectedDtoAll = List.of(BookingMapper.toDto(booking1));
        List<BookingDto> expectedDtoFuture = List.of(BookingMapper.toDto(bookingFuture));
        List<BookingDto> expectedDtoPast = List.of(BookingMapper.toDto(bookingPast));
        List<BookingDto> expectedDtoCurrent = List.of(BookingMapper.toDto(bookingCurrent));
        List<BookingDto> expectedDtoDefault = List.of(BookingMapper.toDto(bookingApproved));

        List<BookingDto> dtoAll = bookingServiceStandard
                .getBookingsByOwner(myUser.getId(), "ALL", null, null);
        List<BookingDto> dtoFuture = bookingServiceStandard
                .getBookingsByOwner(myUser.getId(), "FUTURE", null, null);
        List<BookingDto> dtoPast = bookingServiceStandard
                .getBookingsByOwner(myUser.getId(), "PAST", null, null);
        List<BookingDto> dtoCurrent = bookingServiceStandard
                .getBookingsByOwner(myUser.getId(), "CURRENT", null, null);
        List<BookingDto> dtoDefault = bookingServiceStandard
                .getBookingsByOwner(myUser.getId(), "APPROVED", null, null);

        assertEquals(expectedDtoAll, dtoAll);
        assertEquals(expectedDtoFuture, dtoFuture);
        assertEquals(expectedDtoPast, dtoPast);
        assertEquals(expectedDtoCurrent, dtoCurrent);
        assertEquals(expectedDtoDefault, dtoDefault);
    }

    @Test
    void getBookingsByOwner_whenOwnerExistsAndStateIsValidAndWithPagination_thenReturnListOfBookingDto() {
        Integer from = 0;
        Integer size = 10;
        Pageable pagination = PageRequest.of(from / size, size);
        when(userRepository.existsById(myUser.getId())).thenReturn(true);
        when(bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(myUser.getId(), pagination))
                .thenReturn(new PageImpl<>(List.of(booking1)));
        when(bookingRepository.findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(
                eq(myUser.getId()),
                any(LocalDateTime.class),
                eq(pagination))
        ).thenReturn(new PageImpl<>(List.of(bookingFuture)));
        when(bookingRepository.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(
                eq(myUser.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(pagination))
        ).thenReturn(new PageImpl<>(List.of(bookingPast)));
        when(bookingRepository.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                eq(myUser.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(pagination))
        ).thenReturn(new PageImpl<>(List.of(bookingCurrent)));
        when(bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(
                myUser.getId(),
                BookingStatus.APPROVED,
                pagination)
        ).thenReturn(new PageImpl<>(List.of(bookingApproved)));
        List<BookingDto> expectedDtoAll = List.of(BookingMapper.toDto(booking1));
        List<BookingDto> expectedDtoFuture = List.of(BookingMapper.toDto(bookingFuture));
        List<BookingDto> expectedDtoPast = List.of(BookingMapper.toDto(bookingPast));
        List<BookingDto> expectedDtoCurrent = List.of(BookingMapper.toDto(bookingCurrent));
        List<BookingDto> expectedDtoDefault = List.of(BookingMapper.toDto(bookingApproved));

        List<BookingDto> dtoAll = bookingServiceStandard
                .getBookingsByOwner(myUser.getId(), "ALL", from, size);
        List<BookingDto> dtoFuture = bookingServiceStandard
                .getBookingsByOwner(myUser.getId(), "FUTURE", from, size);
        List<BookingDto> dtoPast = bookingServiceStandard
                .getBookingsByOwner(myUser.getId(), "PAST", from, size);
        List<BookingDto> dtoCurrent = bookingServiceStandard
                .getBookingsByOwner(myUser.getId(), "CURRENT", from, size);
        List<BookingDto> dtoDefault = bookingServiceStandard
                .getBookingsByOwner(myUser.getId(), "APPROVED", from, size);

        assertEquals(expectedDtoAll, dtoAll);
        assertEquals(expectedDtoFuture, dtoFuture);
        assertEquals(expectedDtoPast, dtoPast);
        assertEquals(expectedDtoCurrent, dtoCurrent);
        assertEquals(expectedDtoDefault, dtoDefault);
    }

    @Test
    void getBookingsByOwner_whenBookerNotExists_thenThrowNotFoundException() {
        when(userRepository.existsById(myUser.getId())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingServiceStandard.getBookingsByOwner(
                        myUser.getId(),
                        "ALL",
                        null,
                        null
                )
        );
    }

    @Test
    void getBookingsByOwner_whenStateIsUnsupported_thenThrowUnsupportedBookingStatusException() {
        when(userRepository.existsById(myUser.getId())).thenReturn(true);

        assertThrows(UnsupportedBookingStatusException.class,
                () -> bookingServiceStandard.getBookingsByOwner(
                        myUser.getId(),
                        "UNSUPPORTED_STATE",
                        null,
                        null
                )
        );
    }

    @Test
    void getBookingsByOwner_whenPaginationIsNotValid_thenThrowValidationException() {
        when(userRepository.existsById(myUser.getId())).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> bookingServiceStandard.getBookingsByOwner(
                        myUser.getId(),
                        "ALL",
                        -1,
                        1
                )
        );
        assertThrows(ValidationException.class,
                () -> bookingServiceStandard.getBookingsByOwner(
                        myUser.getId(),
                        "ALL",
                        1,
                        -1
                )
        );
        assertThrows(ValidationException.class,
                () -> bookingServiceStandard.getBookingsByOwner(
                        myUser.getId(),
                        "ALL",
                        1,
                        0
                )
        );
    }
}