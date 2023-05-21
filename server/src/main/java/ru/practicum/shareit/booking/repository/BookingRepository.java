package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBooker_IdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    Page<Booking> findBookingsByBooker_IdAndStatusOrderByStartDesc(Long bookerId,
                                                                   BookingStatus status,
                                                                   Pageable pageable);

    List<Booking> findBookingsByBooker_IdOrderByStartDesc(Long bookerId);

    Page<Booking> findBookingsByBooker_IdOrderByStartDesc(Long bookerId, Pageable pageable);

    List<Booking> findAllByBooker_IdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    Page<Booking> findAllByBooker_IdAndStartIsAfterOrderByStartDesc(Long bookerId,
                                                                    LocalDateTime start,
                                                                    Pageable pageable);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(Long bookerId,
                                                                                   LocalDateTime start,
                                                                                   LocalDateTime end);

    Page<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(Long bookerId,
                                                                                   LocalDateTime start,
                                                                                   LocalDateTime end,
                                                                                   Pageable pageable);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId,
                                                                                  LocalDateTime start,
                                                                                  LocalDateTime end);

    Page<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId,
                                                                                  LocalDateTime start,
                                                                                  LocalDateTime end,
                                                                                  Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    Page<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status, Pageable pageable);

    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(Long ownerId);

    Page<Booking> findAllByItem_Owner_IdOrderByStartDesc(Long ownerId, Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(Long ownerId, LocalDateTime start);

    Page<Booking> findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(Long ownerId,
                                                                        LocalDateTime start,
                                                                        Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(Long ownerId,
                                                                                       LocalDateTime start,
                                                                                       LocalDateTime end);

    Page<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(Long ownerId,
                                                                                       LocalDateTime start,
                                                                                       LocalDateTime end,
                                                                                       Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long ownerId,
                                                                                      LocalDateTime start,
                                                                                      LocalDateTime end);

    Page<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long ownerId,
                                                                                      LocalDateTime start,
                                                                                      LocalDateTime end,
                                                                                      Pageable pageable);

    List<Booking> findByItem_IdAndItem_Owner_IdAndStatusNotOrderByStartAsc(Long itemId,
                                                                           Long itemOwner,
                                                                           BookingStatus status);

    List<Booking> findAllByItem_IdAndBooker_IdAndStatusNotAndStartBefore(Long itemId,
                                                                         Long ownerId,
                                                                         BookingStatus status,
                                                                         LocalDateTime now);

    List<Booking> findAllByItem_IdInOrderByStartAsc(Set<Long> itemsId);
}
