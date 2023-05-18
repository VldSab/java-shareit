package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Test
    void findAllByRequester_IdOrderByCreatedDesc() {
    }

    @Test
    void findAllByRequester_IdNotOrderByCreatedDesc() {
    }
}