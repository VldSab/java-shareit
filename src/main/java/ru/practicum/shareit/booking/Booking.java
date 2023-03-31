package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.constants.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    Long id;
    Item item;
    User booker;
    User owner;
    LocalDate startDate;
    LocalDate endDate;
    BookingStatus isAccepted;
    String review;
}
