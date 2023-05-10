package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentReceived;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    ItemService itemService;
    @InjectMocks
    ItemController itemController;

    private static final User myUser = User.builder()
            .id(1L)
            .email("user@gmail.com")
            .name("John")
            .build();
    private static final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .owner(myUser)
            .description("Simple item")
            .isAvailable(true)
            .build();
    private static final Comment comment = Comment.builder()
            .id(1L)
            .item(item)
            .author(myUser)
            .text("Good one")
            .build();

    private static final ItemDto itemDto = ItemMapper.toDto(item);

    @Test
    void addItem_whenInvoked_thenReturnItemDto() {
        when(itemService.addItem(item, myUser.getId())).thenReturn(itemDto);

        assertEquals(itemDto, itemController.addItem(myUser.getId(), item));
    }

    @Test
    void addComment_whenInvoked_thenReturnItemDto() {
        CommentReceived commentReceived = new CommentReceived(comment.getText());
        CommentDto commentDto = CommentMapper.toDto(comment);
        when(itemService.addComment(myUser.getId(), item.getId(), commentReceived))
                .thenReturn(commentDto);

        assertEquals(commentDto, itemController.addComment(myUser.getId(), item.getId(), commentReceived));
    }

    @Test
    void updateItem_whenInvoked_thenReturnItemDto() {
        ItemDto itemDto = ItemMapper.toDto(item);
        when(itemService.updateItem(item.getId(), item, myUser.getId())).thenReturn(itemDto);

        assertEquals(itemDto, itemController.updateItem(item.getId(), myUser.getId(), item));
    }

    @Test
    void getItemById_whenInvoked_thenReturnItemInfoDto() {
        ItemInfoDto itemInfoDto = ItemMapper
                .toInfoDto(item, List.of(CommentMapper.toDto(comment)), null, null);
        when(itemService.getItemById(item.getId(), myUser.getId())).thenReturn(itemInfoDto);

        assertEquals(itemInfoDto, itemController.getItemById(myUser.getId(), item.getId()));
    }

    @Test
    void getUserItems_whenInvoked_thenReturnListOfItemInfoDto() {
        List<ItemInfoDto> itemDtoList = List.of(
                ItemMapper.toInfoDto(item, List.of(CommentMapper.toDto(comment)), null, null)
        );
        when(itemService.getUserItems(myUser.getId(), null, null)).thenReturn(itemDtoList);

        assertEquals(itemDtoList, itemController.getUserItems(myUser.getId(), null, null));
    }

    @Test
    void getItemsByContent_whenContentNotNulAndNotBlank_thenReturnListOfItemDto() {
        String content = "content";
        List<ItemDto> itemDtoList = List.of(ItemMapper.toDto(item));
        when(itemService.getItemByContent(content, myUser.getId(), null, null))
                .thenReturn(itemDtoList);

        assertEquals(itemDtoList, itemController.getItemsByContent(content, myUser.getId(), null, null));
    }

    @Test
    void getItemsByContent_whenContentIsNull_thenReturnEmptyList() {
        assertEquals(
                0,
                itemController.getItemsByContent(null, myUser.getId(), null, null).size()
        );
    }
}