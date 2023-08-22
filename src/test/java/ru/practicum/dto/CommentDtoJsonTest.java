package ru.practicum.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.comment.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeCommentDto() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Test Comment")
                .authorName("Test Author")
                .created(LocalDateTime.parse("2023-08-16T12:00:00"))
                .build();

        String json = objectMapper.writeValueAsString(commentDto);

        assertThat(json).isEqualTo("{\"id\":1,\"text\":\"Test Comment\",\"authorName\":\"Test Author\",\"created\":\"2023-08-16T12:00:00\"}");
    }

    @Test
    void testDeserializeCommentDto() throws Exception {
        String json = "{\"id\":1,\"text\":\"Test Comment\",\"authorName\":\"Test Author\",\"created\":\"2023-08-16T12:00:00\"}";

        CommentDto commentDto = objectMapper.readValue(json, CommentDto.class);

        assertThat(commentDto.getId()).isEqualTo(1L);
        assertThat(commentDto.getText()).isEqualTo("Test Comment");
        assertThat(commentDto.getAuthorName()).isEqualTo("Test Author");
        assertThat(commentDto.getCreated()).isEqualTo(LocalDateTime.parse("2023-08-16T12:00:00"));
    }
}
