package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DBItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.DBUserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Items service implementation.
 *
 * @see ItemService
 */
@Service
@RequiredArgsConstructor
public class ItemServiceStandard implements ItemService {

    private final DBItemRepository itemRepository;
    private final DBUserRepository userRepository;

    @Override
    public ItemDto addItem(Item item, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        item.setOwner(user.get());
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long id, Item item, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        Optional<Item> cur = itemRepository.findById(id);
        if (cur.isEmpty())
            throw new NotFoundException("Предмета с id " + id + " не существует");
        if (!Objects.equals(cur.get().getOwner().getId(), userId))
            throw new NotFoundException("У предмета с id " + id + " другой владелец");

        Item curentItem = cur.get();

        if (item.getOwner() != null)
            curentItem.setOwner(item.getOwner());
        if (item.getIsAvailable() != null)
            curentItem.setIsAvailable(item.getIsAvailable());
        if (item.getName() != null)
            curentItem.setName(item.getName());
        if (item.getDescription() != null)
            curentItem.setDescription(item.getDescription());
        curentItem.setOwner(user.get());

        return ItemMapper.toDto(itemRepository.save(curentItem));
    }

    @Override
    public ItemDto getItemById(Long id, Long userId) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty())
            throw new NotFoundException("Предмета с id " + id + " не существует");
        return ItemMapper.toDto(item.get());
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        // использую join fetch, stream безопасен
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemByContent(String content, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        // нет обращения к User, stream безопасен
        return itemRepository.findByContent(content).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
