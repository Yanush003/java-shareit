package ru.practicum.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.booking.BookingController;
import ru.practicum.booking.BookingDto;
import ru.practicum.booking.BookingService;
import ru.practicum.booking.Status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingDto createSampleBookingDto() {
        return BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .itemId(10L)
                .build();
    }

    @Test
    public void testAddBooking() throws Exception {
        BookingDto bookingDto = createSampleBookingDto();
        when(bookingService.add(Mockito.any(BookingDto.class), Mockito.anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())));
    }

    @Test
    public void testUpdateStatus() throws Exception {
        long bookingId = 1L;
        boolean approved = true;
        BookingDto updatedBookingDto = createSampleBookingDto();
        updatedBookingDto.setStatus(Status.APPROVED);
        when(bookingService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(updatedBookingDto);
        mockMvc.perform(patch("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", Boolean.toString(approved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(updatedBookingDto.getStatus().toString())));
    }

    @Test
    public void testFindById() throws Exception {
        Long bookingId = 1L;
        BookingDto bookingDto = createSampleBookingDto();
        when(bookingService.getByUserId(bookingId, 1L))
                .thenReturn(bookingDto);
        mockMvc.perform(get("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())));
    }

    @Test
    public void testGetAllBooking() throws Exception {
        List<BookingDto> bookings = Collections.singletonList(createSampleBookingDto());
        when(bookingService.getAllBooking(1L, "ALL", 0, 100)).thenReturn(bookings);
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookings.get(0).getId().intValue())));
    }

    @Test
    public void testGetOwnerBooking() throws Exception {
        List<BookingDto> bookings = Collections.singletonList(createSampleBookingDto());
        when(bookingService.getOwnerBooking(1L, "ALL", 0, 100)).thenReturn(bookings);
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookings.get(0).getId().intValue())));
    }
}
