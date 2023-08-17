package ru.practicum.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.request.ItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeItemRequestDto() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Test Description")
                .requester(null)
                .created(LocalDateTime.parse("2023-08-16T12:00:00"))
                .build();

        String json = objectMapper.writeValueAsString(itemRequestDto);

        assertThat(json).isEqualTo("{\"id\":1,\"description\":\"Test Description\",\"requester\":null,\"created\":\"2023-08-16T12:00:00\"}");
    }

    @Test
    void testDeserializeItemRequestDto() throws Exception {
        String json = "{\"id\":1,\"description\":\"Test Description\",\"requester\":null,\"created\":\"2023-08-16T12:00:00\"}";

        ItemRequestDto itemRequestDto = objectMapper.readValue(json, ItemRequestDto.class);

        assertThat(itemRequestDto.getId()).isEqualTo(1L);
        assertThat(itemRequestDto.getDescription()).isEqualTo("Test Description");
        assertThat(itemRequestDto.getRequester()).isNull();
        assertThat(itemRequestDto.getCreated()).isEqualTo(LocalDateTime.parse("2023-08-16T12:00:00"));
    }
}
