package ru.practicum.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.comment.CommentDto;
import ru.practicum.item.ItemController;
import ru.practicum.item.ItemDto;
import ru.practicum.item.ItemService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testGetItem() throws Exception {
        ItemDto itemDto = createSampleItemDto();
        when(itemService.getById(1L, 1L)).thenReturn(itemDto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId().intValue())));
    }

    @Test
    public void testCreateItem() throws Exception {
        ItemDto itemDto = createSampleItemDto();
        when(itemService.add(1L, itemDto)).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(createSampleItemDto()))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId().intValue())));
    }

    @Test
    public void testUpdateItem() throws Exception {
        ItemDto itemDto = createSampleItemDto();
        when(itemService.update(1L, 1L, itemDto)).thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(createSampleItemDto()))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId().intValue())));
    }

    @Test
    public void testSearchItems() throws Exception {
        List<ItemDto> items = Collections.singletonList(createSampleItemDto());
        when(itemService.search("sample")).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .param("text", "sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(items.get(0).getId().intValue())));
    }

    @Test
    public void testGetAllItems() throws Exception {
        List<ItemDto> items = Collections.singletonList(createSampleItemDto());
        when(itemService.getAll(1L)).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(items.get(0).getId().intValue())));
    }

    @Test
    public void testCreateComment() throws Exception {
        CommentDto commentDto = createSampleCommentDto();
        CommentDto dto = CommentDto.builder()
                .text("Sample Comment")
                .authorName("Author")
                .created(LocalDateTime.now())
                .build();
        when(itemService.addComment(1L, 1L, dto)).thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(createSampleCommentDto()))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    private ItemDto createSampleItemDto() {
        return ItemDto.builder()
                .id(1L)
                .name("Sample Item")
                .description("This is a sample item.")
                .available(true)
                .build();
    }

    private CommentDto createSampleCommentDto() {
        return CommentDto.builder()
                .id(1L)
                .text("Sample Comment")
                .authorName("Author")
                .created(LocalDateTime.now())
                .build();
    }
}
