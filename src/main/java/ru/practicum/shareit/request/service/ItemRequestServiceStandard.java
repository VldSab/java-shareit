package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemResponse;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceStandard implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto addItemRequest(Long requesterId, ItemRequest itemRequest) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("Не существует пользователя с id " + requesterId));
        itemRequest.setRequester(requester);
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestInfoDto> getAllRequestersRequests(Long requesterId) {
        if (!userRepository.existsById(requesterId))
            throw new NotFoundException("Не существует пользователя с id " + requesterId);
        // берем все предметы с непустым полем requestId и получаем список откликов
        List<ItemResponse> allResponses = itemRepository.findAllByRequestIdNotNull().stream()
                .map(ItemRequestMapper::toResponse)
                .collect(Collectors.toList());
        // получаем все запросы текущего пользователя
        List<ItemRequest> itemRequestsByRequester = itemRequestRepository
                .findAllByRequester_IdOrderByCreatedDesc(requesterId);
        // для каждого запроса текущего пользователя собираем список откликов и готовим результат
        return itemRequestsByRequester.stream()
                .map(it -> {
                    List<ItemResponse> responsesForCurrentRequest = allResponses.stream()
                            .filter(response -> Objects.equals(response.getRequestId(), it.getId()))
                            .collect(Collectors.toList());
                    return ItemRequestMapper.toInfoDto(it, responsesForCurrentRequest);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestInfoDto> getAllOtherUsersRequests(Long requesterId, Integer from, Integer size) {
        if (!userRepository.existsById(requesterId))
            throw new NotFoundException("Не существует пользователя с id " + requesterId);
        // получаем все чужие запросы
        List<ItemRequest> itemRequests;
        if (from != null && size != null) {
            if (from < 0 || size <= 0)
                throw new ValidationException("Размер страницы и интекс начала не могут быть меньше нуля");
            int pageNumber = from / size;
            Pageable pagination = PageRequest.of(pageNumber, size);
            Page<ItemRequest> pageItemRequests = itemRequestRepository
                    .findAllByRequester_IdNotOrderByCreatedDesc(requesterId, pagination);
            itemRequests = pageItemRequests.getContent();
        } else {
            itemRequests = itemRequestRepository.findAllByRequester_IdOrderByCreatedDesc(requesterId);
        }
        // получаем все ответы отсортированные от новых к старым
        List<ItemResponse> itemResponses = itemRepository.findAllByRequestIdNotNull().stream()
                .map(ItemRequestMapper::toResponse)
                .collect(Collectors.toList());
        // для каждого запроса собираем список ответов
        return itemRequests.stream()
                .map(request -> {
                    List<ItemResponse> responses = itemResponses.stream()
                            .filter(itemResponse -> Objects.equals(itemResponse.getRequestId(), request.getId()))
                            .collect(Collectors.toList());
                    return ItemRequestMapper.toInfoDto(request, responses);
                })
                .collect(Collectors.toList());

    }

    @Override
    public ItemRequestInfoDto getById(Long requesterId, Long requestId) {
        if (!userRepository.existsById(requesterId))
            throw new NotFoundException("Не существует пользователя с id " + requesterId);
        // получаем запрос
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не существует запроса с id " + requestId));
        // получаем ответы на этот запрос
        List<ItemResponse> responses = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemRequestMapper::toResponse)
                .collect(Collectors.toList());
        return ItemRequestMapper.toInfoDto(itemRequest, responses);
    }
}
