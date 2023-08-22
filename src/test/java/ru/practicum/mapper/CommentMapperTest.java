package ru.practicum.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.comment.Comment;
import ru.practicum.comment.CommentDto;
import ru.practicum.comment.CommentMapper;
import ru.practicum.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentMapperTest {

    private final CommentMapper commentMapper = new CommentMapper();

    @Test
    public void testToCommentDto() {
        Comment comment = createTestComment();

        CommentDto commentDto = commentMapper.toCommentDto(comment);

        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getAuthor().getName(), commentDto.getAuthorName());
        assertEquals(comment.getCreated(), commentDto.getCreated());
    }

    @Test
    public void testToComment() {
        CommentDto commentDto = createTestCommentDto();

        Comment comment = commentMapper.toComment(commentDto);

        assertEquals(commentDto.getText(), comment.getText());
        assertNotNull(comment.getCreated()); // Comment creation time should be set
    }

    private Comment createTestComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");
        comment.setAuthor(User.builder().id(1L).name("Test User").email("test@example.com").build());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    private CommentDto createTestCommentDto() {
        return CommentDto.builder()
                .text("Test comment")
                .authorName("Test User")
                .build();
    }
}
