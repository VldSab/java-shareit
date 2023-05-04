package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Items service implementation.
 *
 * @see ItemService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceStandard implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

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

    private Booking previousBookingInSorted(List<Booking> bookingsList) {
        if (bookingsList == null || bookingsList.isEmpty())
            return null;
        LocalDateTime now = LocalDateTime.now();
        Booking prev = null;
        Booking cur = null;
        for (Booking booking : bookingsList) {
            cur = booking;
            if (prev != null && (cur.getStart().isAfter(now)))
                return prev;
            prev = cur;
        }
        return cur;
    }

    private Booking nextBookingInSorted(List<Booking> bookingsList) {
        if (bookingsList == null || bookingsList.isEmpty())
            return null;
        LocalDateTime now = LocalDateTime.now();
        for (Booking cur : bookingsList) {
            if (cur.getStart().isAfter(now))
                return cur;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemInfoDto getItemById(Long id, Long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Предмета с id " + id + " не существует"));

        List<Booking> bookings = bookingRepository.findByItem_IdAndItem_Owner_IdAndStatusNotOrderByStartAsc(id, userId, BookingStatus.REJECTED);
        List<CommentDto> comments = commentRepository
                .findAllByItem_IdOrderByCreated(id)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());

        return ItemMapper.toInfoDto(item, comments, previousBookingInSorted(bookings), nextBookingInSorted(bookings));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemInfoDto> getUserItems(Long userId) {
        if (!userRepository.existsById(userId))
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        // получить все Items
        List<Item> items = itemRepository.findAllByOwner_IdOrderById(userId);
        // получить сет идентификаторов Items
        Set<Long> itemsIds = items.stream().map(Item::getId).collect(Collectors.toSet());
        // получить комментарии по каждому Item
        Map<Long, List<Comment>> comments = commentRepository
                .findAllByItem_IdInOrderByItem_Id(itemsIds)
                .stream()
                .collect(Collectors.groupingBy(it -> it.getItem().getId()));
        // получить букинг по каждому Item
        Map<Long, List<Booking>> itemIdToBookings = bookingRepository
                .findAllByItem_IdInOrderByStartAsc(itemsIds)
                .stream()
                .collect(Collectors.groupingBy(it -> it.getItem().getId()));
        // сформировать итоговый спиок ItemInfoDto
        List<ItemInfoDto> result = new ArrayList<>();
        for (Item item : items) {
            ItemInfoDto itemInfoDto = ItemMapper.toInfoDto(
                    item,
                    comments.getOrDefault(item.getId(), List.of()).stream().map(CommentMapper::toDto).collect(Collectors.toList()),
                    previousBookingInSorted(itemIdToBookings.getOrDefault(item.getId(), List.of())),
                    nextBookingInSorted(itemIdToBookings.getOrDefault(item.getId(), List.of()))
            );
            result.add(itemInfoDto);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemByContent(String content, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id " + userId + " не существует"));
        // нет обращения к User, stream безопасен
        return itemRepository.findByContent(content).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Long authorId, Long itemId, CommentReceived commentReceived) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id " + authorId + " не существует"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмета с id " + itemId + " не существует"));
        List<Booking> bookingList = bookingRepository
                .findAllByItem_IdAndBooker_IdAndStatusNotAndStartBefore(itemId, authorId, BookingStatus.REJECTED, LocalDateTime.now());
        if (bookingList.isEmpty())
            throw new ValidationException("Пользователь не использовал предмет и не может оставить комментарий");
        Comment comment = Comment.builder()
                .text(commentReceived.getText())
                .author(author)
                .item(item)
                .created(LocalDateTime.now())
                .build();
        return CommentMapper.toDto(commentRepository.save(comment));
    }
}
