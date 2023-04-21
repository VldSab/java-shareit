package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.DBUserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceStandard implements BookingService {

    private final BookingRepository bookingRepository;
    private final DBUserRepository userRepository;
    private final DBItemRepository itemRepository;

    @Override
    public BookingDto save(Long userId, ExternalBookingDto booking) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id " + userId + " не существует"));
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Item с id " + booking.getItemId() + " не существует"));
        if (!item.getIsAvailable())
            throw new ObjectUnavailableException("Item с id " + booking.getItemId() + " недоступен");
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Владелец не может забронировать свой item.");
        }

        long start = booking.getStart().toEpochSecond(ZoneOffset.UTC);
        long end = booking.getEnd().toEpochSecond(ZoneOffset.UTC);
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        if (end <= start || end < now || start < now)
            throw new ValidationException("Неверные даты начала и окончания бронирования");

        Booking bookingEntity = Booking.builder()
                .id(booking.getId())
                .booker(user)
                .item(item)
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(BookingStatus.WAITING)
                .build();

        return BookingMapper.toDto(bookingRepository.save(bookingEntity));
    }

    @Override
    public BookingDto approve(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Нет брони с таким id " + bookingId));
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Нет владельца с id " + ownerId));
        if (!Objects.equals(booking.getItem().getOwner().getId(), ownerId))
            throw new NotFoundException("Пользователь " + ownerId + "не является владельцем предмета. " +
                    "Подтвердить бронирование может только владелец.");
        if (!booking.getStatus().equals(BookingStatus.WAITING))
            throw new ValidationException("Можно согласовать только бронь ожидающую подтверждения");
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Нет брони с таким id " + bookingId));
        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();
        if (!Objects.equals(userId, ownerId) && !Objects.equals(userId, bookerId))
            throw new NotFoundException("Получение данных о бронировании может быть выполнено либо автором " +
                    "бронирования, либо владельцем вещи, к которой относится бронирование.");
        return BookingMapper.toDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByBookerId(Long bookerId, String state) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Нет пользователя с таким id " + bookerId));
        if (!BookingStatus.getSet().contains(state))
            throw new UnsupportedBookingStatusException("Unknown state: " + state);
        BookingStatus status = BookingStatus.valueOf(state);
        List<Booking> bookings;
        switch (status) {
            case ALL:
                bookings = bookingRepository.findBookingsByBooker_IdOrderByStartDesc(bookerId);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(bookerId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(
                        bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                break;
            default:
                bookings = bookingRepository.findBookingsByBooker_IdAndStatusOrderByStartDesc(bookerId, status);
        }

        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByOwner(Long ownerId, String state) {
        if (!BookingStatus.getSet().contains(state))
            throw new UnsupportedBookingStatusException("Unknown state: " + state);
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Нет пользователя с таким id " + ownerId));
        BookingStatus status = BookingStatus.valueOf(state);
        List<Booking> bookings;
        switch (status) {
            case ALL:
                bookings = bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(ownerId);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(
                        ownerId,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        ownerId,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                break;
            default:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, status);
        }

        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }
}
