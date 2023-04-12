package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Items service implementation.
 * @see ItemService
 */
@Service
@RequiredArgsConstructor
public class ItemServiceStandard implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(Item item, Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        item.setOwner(user.get());
        return ItemMapper.toDto(itemRepository.addItem(item));
    }

    @Override
    public ItemDto updateItem(Long id, Item item, Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        if (itemRepository.getItemById(id).isEmpty())
            throw new NotFoundException("Предмета с id " + id + " не существует");
        if (!Objects.equals(itemRepository.getItemById(id).get().getOwner().getId(), userId))
            throw new NotFoundException("У предмета с id " + id + " другой владелец");
        item.setOwner(user.get());
        return ItemMapper.toDto(itemRepository.updateItem(id, item));
    }

    @Override
    public ItemDto getItemById(Long id, Long userId) {
        Optional<Item> item = itemRepository.getItemById(id);
        if (item.isEmpty())
            throw new NotFoundException("Предмета с id " + id + " не существует");
        return ItemMapper.toDto(item.get());
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        return itemRepository.getUserItems(userId).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemByContent(String content, Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        return itemRepository.getItemByContent(content).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
