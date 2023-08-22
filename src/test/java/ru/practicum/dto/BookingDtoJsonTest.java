package ru.practicum.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.booking.BookingDto;
import ru.practicum.booking.Status;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeBookingDto() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.parse("2023-08-16T12:00:00"))
                .end(LocalDateTime.parse("2023-08-16T14:00:00"))
                .itemId(2L)
                .status(Status.APPROVED)
                .state(Status.WAITING)
                .build();

        String json = objectMapper.writeValueAsString(bookingDto);

        assertThat(json).isEqualTo("{\"id\":1,\"start\":\"2023-08-16T12:00:00\",\"end\":\"2023-08-16T14:00:00\",\"itemId\":2,\"item\":null,\"status\":\"APPROVED\",\"booker\":null,\"state\":\"WAITING\"}");
    }

    @Test
    void testDeserializeBookingDto() throws Exception {
        String json = "{\"id\":1,\"start\":\"2023-08-16T12:00:00\",\"end\":\"2023-08-16T14:00:00\",\"itemId\":2,\"status\":\"APPROVED\",\"booker\":null,\"state\":\"WAITING\"}";

        BookingDto bookingDto = objectMapper.readValue(json, BookingDto.class);

        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStart()).isEqualTo(LocalDateTime.parse("2023-08-16T12:00:00"));
        assertThat(bookingDto.getEnd()).isEqualTo(LocalDateTime.parse("2023-08-16T14:00:00"));
        assertThat(bookingDto.getItemId()).isEqualTo(2L);
        assertThat(bookingDto.getStatus()).isEqualTo(Status.APPROVED);
        assertThat(bookingDto.getState()).isEqualTo(Status.WAITING);
    }
}

