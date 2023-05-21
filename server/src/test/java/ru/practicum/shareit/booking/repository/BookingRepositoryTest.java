package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookingRepositoryTest {

    @Test
    void findBookingsByBooker_IdAndStatusOrderByStartDesc() {
    }

    @Test
    void testFindBookingsByBooker_IdAndStatusOrderByStartDesc() {
    }

    @Test
    void findBookingsByBooker_IdOrderByStartDesc() {
    }

    @Test
    void testFindBookingsByBooker_IdOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdAndStartIsAfterOrderByStartDesc() {
    }

    @Test
    void testFindAllByBooker_IdAndStartIsAfterOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc() {
    }

    @Test
    void testFindAllByBooker_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
    }

    @Test
    void testFindAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
    }

    @Test
    void findAllByItem_Owner_IdAndStatusOrderByStartDesc() {
    }

    @Test
    void testFindAllByItem_Owner_IdAndStatusOrderByStartDesc() {
    }

    @Test
    void findAllByItem_Owner_IdOrderByStartDesc() {
    }

    @Test
    void testFindAllByItem_Owner_IdOrderByStartDesc() {
    }

    @Test
    void findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc() {
    }

    @Test
    void testFindAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc() {
    }

    @Test
    void findAllByItem_Owner_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc() {
    }

    @Test
    void testFindAllByItem_Owner_IdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
    }

    @Test
    void testFindAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
    }

    @Test
    void findByItem_IdAndItem_Owner_IdAndStatusNotOrderByStartAsc() {
    }

    @Test
    void findAllByItem_IdAndBooker_IdAndStatusNotAndStartBefore() {
    }

    @Test
    void findAllByItem_IdInOrderByStartAsc() {
    }
}