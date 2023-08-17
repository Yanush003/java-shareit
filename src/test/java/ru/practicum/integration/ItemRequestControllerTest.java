package ru.practicum.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private final Long TEST_USER_ID = 1L;
    private final Long TEST_REQUEST_ID = 100L;
    private final ItemRequestDto mockRequestDto = ItemRequestDto.builder()
            .id(TEST_REQUEST_ID)
            .description("Test description")
            .requester(new User())
            .created(LocalDateTime.now())
            .build();
    private final ItemRequestWithAnswersDto mockRequestWithAnswersDto = ItemRequestWithAnswersDto.builder()
            .id(TEST_REQUEST_ID)
            .description("Test description")
            .created(LocalDateTime.now())
            .items(Collections.singletonList(AnswerDto.builder().build()))
            .build();

    @Before
    public void setUp() {
        when(itemRequestService.addRequest(eq(TEST_USER_ID), any())).thenReturn(mockRequestDto);
        when(itemRequestService.getYourListRequests(TEST_USER_ID)).thenReturn(Collections.singletonList(mockRequestWithAnswersDto));
        when(itemRequestService.getOtherListRequests(eq(TEST_USER_ID), anyInt(), anyInt())).thenReturn(Collections.singletonList(mockRequestWithAnswersDto));
        when(itemRequestService.getItemRequest(TEST_USER_ID, TEST_REQUEST_ID)).thenReturn(mockRequestWithAnswersDto);
    }

    @Test
    public void addRequestTest() throws Exception {
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Test description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_REQUEST_ID));
    }

    @Test
    public void getYourListRequestsTest() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(TEST_REQUEST_ID));
    }

    @Test
    public void getOtherListRequestsTest() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", TEST_USER_ID)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(TEST_REQUEST_ID));
    }

    @Test
    public void getItemRequestTest() throws Exception {
        mockMvc.perform(get("/requests/" + TEST_REQUEST_ID)
                        .header("X-Sharer-User-Id", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_REQUEST_ID));
    }
}
