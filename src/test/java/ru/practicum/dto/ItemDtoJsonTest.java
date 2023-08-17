package ru.practicum.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.item.ItemDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeItemDto() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .owner(null)
                .request(null)
                .requestId(null)
                .lastBooking(null)
                .nextBooking(null)
                .comments(new ArrayList<>())
                .build();

        String json = objectMapper.writeValueAsString(itemDto);

        assertThat(json).isEqualTo("{\"id\":1,\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true,\"owner\":null,\"request\":null,\"requestId\":null,\"lastBooking\":null,\"nextBooking\":null,\"comments\":[]}");
    }

    @Test
    void testDeserializeItemDto() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true,\"owner\":null,\"request\":null,\"requestId\":null,\"lastBooking\":null,\"nextBooking\":null,\"comments\":[]}";

        ItemDto itemDto = objectMapper.readValue(json, ItemDto.class);

        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Test Item");
        assertThat(itemDto.getDescription()).isEqualTo("Test Description");
        assertThat(itemDto.getAvailable()).isEqualTo(true);
        assertThat(itemDto.getOwner()).isNull();
        assertThat(itemDto.getRequest()).isNull();
        assertThat(itemDto.getRequestId()).isNull();
        assertThat(itemDto.getLastBooking()).isNull();
        assertThat(itemDto.getNextBooking()).isNull();
        assertThat(itemDto.getComments()).isInstanceOf(List.class).isEmpty();
    }
}

