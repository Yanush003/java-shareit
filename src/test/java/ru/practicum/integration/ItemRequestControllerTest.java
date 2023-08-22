package ru.practicum.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.request.AnswerDto;
import ru.practicum.request.ItemRequestDto;
import ru.practicum.request.ItemRequestService;
import ru.practicum.request.ItemRequestWithAnswersDto;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private final Long testUserId = 1L;
    private final Long testRequestId = 100L;
    private final ItemRequestDto mockRequestDto = ItemRequestDto.builder()
            .id(testRequestId)
            .description("Test description")
            .requester(new User())
            .created(LocalDateTime.now())
            .build();
    private final ItemRequestWithAnswersDto mockRequestWithAnswersDto = ItemRequestWithAnswersDto.builder()
            .id(testRequestId)
            .description("Test description")
            .created(LocalDateTime.now())
            .items(Collections.singletonList(AnswerDto.builder().build()))
            .build();

    @BeforeEach
    public void setUp() {
        when(itemRequestService.addRequest(eq(testUserId), any())).thenReturn(mockRequestDto);
        when(itemRequestService.getYourListRequests(testUserId)).thenReturn(Collections.singletonList(mockRequestWithAnswersDto));
        when(itemRequestService.getOtherListRequests(eq(testUserId), anyInt(), anyInt())).thenReturn(Collections.singletonList(mockRequestWithAnswersDto));
        when(itemRequestService.getItemRequest(testUserId, testRequestId)).thenReturn(mockRequestWithAnswersDto);
    }

    @Test
    public void addRequestTest() throws Exception {
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Test description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testRequestId));
    }

    @Test
    public void getYourListRequestsTest() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testRequestId));
    }

    @Test
    public void getOtherListRequestsTest() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", testUserId)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testRequestId));
    }

    @Test
    public void getItemRequestTest() throws Exception {
        mockMvc.perform(get("/requests/" + testRequestId)
                        .header("X-Sharer-User-Id", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testRequestId));
    }
}
