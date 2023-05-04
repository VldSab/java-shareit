package ru.practicum.shareit.booking;

import java.util.HashSet;
import java.util.Set;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELLED,
    ALL,
    CURRENT,
    FUTURE,
    PAST;

    private static final Set<String> set = new HashSet<>();

    static {
        set.add(BookingStatus.WAITING.name());
        set.add(BookingStatus.APPROVED.name());
        set.add(BookingStatus.REJECTED.name());
        set.add(BookingStatus.CANCELLED.name());
        set.add(BookingStatus.ALL.name());
        set.add(BookingStatus.CURRENT.name());
        set.add(BookingStatus.FUTURE.name());
        set.add(BookingStatus.PAST.name());
    }

    public static Set<String> getSet() {
        return set;
    }
}
