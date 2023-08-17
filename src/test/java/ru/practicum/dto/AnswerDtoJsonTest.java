package ru.practicum.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.request.AnswerDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class AnswerDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeAnswerDto() throws Exception {
        AnswerDto answerDto = AnswerDto.builder()
                .id(1L)
                .name("Test Answer")
                .description("Test Description")
                .available(true)
                .requestId(2L)
                .build();

        String json = objectMapper.writeValueAsString(answerDto);

        assertThat(json).isEqualTo("{\"id\":1,\"name\":\"Test Answer\",\"description\":\"Test Description\",\"available\":true,\"requestId\":2}");
    }

    @Test
    void testDeserializeAnswerDto() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test Answer\",\"description\":\"Test Description\",\"available\":true,\"requestId\":2}";

        AnswerDto answerDto = objectMapper.readValue(json, AnswerDto.class);

        assertThat(answerDto.getId()).isEqualTo(1L);
        assertThat(answerDto.getName()).isEqualTo("Test Answer");
        assertThat(answerDto.getDescription()).isEqualTo("Test Description");
        assertThat(answerDto.getAvailable()).isEqualTo(true);
        assertThat(answerDto.getRequestId()).isEqualTo(2L);
    }
}

