package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
public class ItemRequestInfoDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemResponse> items;
}
