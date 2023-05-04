package ru.practicum.shareit.item.comment;

/**
 * Mapper Comment to DTO.
 * @see Comment
 * @see CommentDto
 */
public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

}
