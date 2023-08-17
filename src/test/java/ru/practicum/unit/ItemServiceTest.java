package ru.practicum.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.booking.BookingRepositoryJpa;
import ru.practicum.comment.CommentRepositoryJpa;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemDto;
import ru.practicum.item.ItemRepositoryJpa;
import ru.practicum.item.ItemService;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.ItemRequestRepositoryJpa;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryJpa;

class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private UserRepositoryJpa userRepository;
    @Mock
    private ItemRepositoryJpa itemRepository;

    @Mock
    private ItemRequestRepositoryJpa itemRequestRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetWithExistingId_thenReturnsItem() {
        Long itemId = 1L;
        Item mockItem = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));

        Item result = itemService.get(itemId);

        assertEquals(mockItem.getName(), result.getName());
    }

    @Test
    void whenGetWithNonExistingId_thenThrowsException() {
        Long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.get(itemId));
    }

    @Test
    void whenAddWithValidDto_thenReturnsItemDto() {
        Long userId = 1L;
        User mockUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
        ItemDto mockItemDto = ItemDto.builder()
                .name("New Item")
                .description("New Item description")
                .available(true)
                .owner(mockUser)
                .requestId(1L)
                .build();
        Item mockItem = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setRequester(mockUser);
        request.setDescription("Test Request for Pagination");
        request.setCreated(LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(itemRepository.save(any(Item.class))).thenReturn(mockItem);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(mockItem));
        when(itemRequestRepository.findById(request.getId())).thenReturn(Optional.of(request));

        ItemDto result = itemService.add(userId, mockItemDto);

        assertNotNull(result);
        assertEquals(mockItem.getId(), result.getId());
        assertEquals(mockItem.getName(), result.getName());
    }

    @Test
    void whenUpdateWithValidDtoAndId_thenReturnsUpdatedDto() {
        Long userId = 1L;
        Long itemId = 1L;
        User mockUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .name("New Item")
                .description("New Item description")
                .available(true)
                .owner(mockUser)
                .requestId(1L)
                .build();
        Item mockItem = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));
        when(itemRepository.save(any(Item.class))).thenReturn(mockItem);
        ItemDto result = itemService.update(userId, itemId, itemDto);
        assertNotNull(result);
        assertEquals(mockItem.getId(), result.getId());
        assertEquals(mockItem.getName(), result.getName());
    }
}

