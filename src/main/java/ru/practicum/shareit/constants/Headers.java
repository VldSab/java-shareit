package ru.practicum.shareit.constants;

public enum Headers {
    USER_ID("X-Sharer-User-Id");

    private final String name;
    Headers(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
