package ru.practicum.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepositoryJpa;
import ru.practicum.booking.Status;
import ru.practicum.comment.Comment;
import ru.practicum.comment.CommentDto;
import ru.practicum.comment.CommentMapper;
import ru.practicum.comment.CommentRepositoryJpa;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.*;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.ItemRequestRepositoryJpa;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private UserRepositoryJpa userRepository;
    @Mock
    private ItemRepositoryJpa itemRepository;
    @Mock
    private ItemRequestRepositoryJpa itemRequestRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private BookingRepositoryJpa bookingRepository;
    @Mock
    private CommentRepositoryJpa commentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetWithExistingId_thenReturnsItem() {
        Long itemId = 1L;
        Item mockItem = createTestItem();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));

        Item result = itemService.get(itemId);
        assertEquals(mockItem.getName(), result.getName());
    }

    @Test
    void whenGetWithNonExistingId_thenThrowsException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.get(1L));
    }

    @Test
    void whenAddWithValidDto_thenReturnsItemDto() {
        User mockUser = createTestUser();
        ItemDto mockItemDto = createTestItemDto(mockUser);
        Item mockItem = createTestItem();

        ItemRequest request = createItemRequest(mockUser);

        setupAddItemMocks(mockUser, mockItem, request);
        when(itemMapper.toItem(mockItemDto)).thenReturn(createTestItem());
        when(itemMapper.toItemDto(mockItem)).thenReturn(mockItemDto);
        ItemDto result = itemService.add(1L, mockItemDto);

        assertNotNull(result);
        assertEquals(mockItem.getName(), result.getName());
    }

    @Test
    void whenUpdateWithValidDtoAndId_thenReturnsUpdatedDto() {
        User mockUser = createTestUser();
        ItemDto itemDto = createTestItemDto(mockUser);
        Item mockItem = createTestItem();

        setupUpdateItemMocks(mockUser, mockItem);
        when(itemMapper.toItemDto(mockItem)).thenReturn(itemDto);
        ItemDto result = itemService.update(1L, 1L, itemDto);

        assertNotNull(result);
        assertEquals(mockItem.getName(), result.getName());
    }

    @Test
    void whenSearchWithMatchingText_thenReturnsList() {
        String searchText = "Test";
        List<Item> items = createTestItemList();
        when(itemRepository.findAll()).thenReturn(items);
        List<ItemDto> result = itemService.search(searchText);
        assertEquals(2, result.size());
    }

    @Test
    void whenGetAll_thenReturnsAllItemsForUser() {
        List<Item> items = createTestItemList();
        User testUser = createTestUser();
        testUser.setId(1L);
        when(itemRepository.findAllByOwner(any(User.class))).thenReturn(items);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookingRepository.findAll()).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDto(any())).thenReturn(createTestItemDto(testUser));
        List<ItemDto> result = itemService.getAll(1L);
        assertEquals(2, result.size());
    }

    @Test
    void whenAddCommentWithValidData_thenReturnsCommentDto() {
        CommentDto commentDto = createTestCommentDto();
        Comment testComment = createTestComment();
        Item item = createTestItem();
        User user = createTestUser();
        setupAddCommentMocks(item, user);
        List<Booking> mockBookings = createTestBookings(user);
        when(bookingRepository.findAllByItemIdAndBookerId(anyLong(), anyLong())).thenReturn(mockBookings);
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(commentMapper.toCommentDto(testComment)).thenReturn(commentDto);
        when(commentMapper.toComment(commentDto)).thenReturn(testComment);
        CommentDto result = itemService.addComment(1L, 1L, commentDto);
        assertEquals(commentDto.getText(), result.getText());
    }

    @Test
    void whenUpdateWithDifferentOwnerId_thenThrowsForbiddenException() {
        ItemDto itemDto = createTestItemDtoWithDifferentOwner();
        Item item = createTestItem();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(NotFoundException.class, () -> itemService.update(1L, 1L, itemDto));
    }

    @Test
    void whenGetByIdWithMatchingUserId_thenReturnsItemDtoWithBookings() {
        Item mockItem = createTestItem();
        ItemDto mockItemDto = createTestItemDto(mockItem.getOwner());
        List<Booking> bookings = createTestBookings(mockItem.getOwner());
        List<Comment> comments = createTestComments(mockItem, mockItem.getOwner());
        setupGetByIdMocks(mockItem, mockItemDto, bookings, comments);
        ItemDto result = itemService.getById(1L, 1L);
        assertNotNull(result);
        assertEquals(mockItem.getName(), result.getName());
    }

    private User createTestUser() {
        return User.builder().id(1L).name("Test User").email("test@example.com").build();
    }

    private Item createTestItem() {
        User owner = User.builder().id(1L).name("Test User").email("test@example.com").build();
        return Item.builder().id(1L).name("Test Item").owner(owner).description("Test Description").available(true).build();
    }

    private ItemDto createTestItemDto(User owner) {
        return ItemDto.builder().name("Test Item").description("New Description").available(true).owner(owner).requestId(1L).build();
    }

    private ItemRequest createItemRequest(User requester) {
        return ItemRequest.builder()
                .id(1L)
                .requester(requester)
                .description("Test Request")
                .created(LocalDateTime.now())
                .build();
    }

    private void setupAddItemMocks(User user, Item item, ItemRequest request) {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(request));
    }

    private void setupUpdateItemMocks(User user, Item item) {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
    }

    private List<Item> createTestItemList() {
        return Arrays.asList(
                Item.builder().id(1L).name("Test Item 1").description("Test Description 1").available(true).build(),
                Item.builder().id(2L).name("Test Item 2").description("Test Description 2").available(true).build()
        );
    }

    private List<ItemDto> createTestItemDtoList() {
        return Arrays.asList(
                ItemDto.builder().id(1L).name("Test Item 1").description("Test Description 1").available(true).build(),
                ItemDto.builder().id(2L).name("Test Item 2").description("Test Description 2").available(true).build()
        );
    }

    private CommentDto createTestCommentDto() {
        return CommentDto.builder().text("Test Comment").build();
    }

    private Comment createTestComment() {
        return Comment.builder().text("Test Comment").build();
    }

    private void setupAddCommentMocks(Item item, User user) {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    }

    private ItemDto createTestItemDtoWithDifferentOwner() {
        return ItemDto.builder().owner(User.builder().id(2L).build()).build();
    }

    private List<Booking> createTestBookings(User booker) {
        LocalDateTime now = LocalDateTime.now();
        return Arrays.asList(
                Booking.builder().id(1L).start(now.minusHours(10)).end(now.plusHours(10)).status(Status.APPROVED).booker(booker).build(),
                Booking.builder().id(2L).start(now.minusHours(10)).end(now.plusHours(10)).status(Status.APPROVED).booker(booker).build()
        );
    }

    private List<Comment> createTestComments(Item commentedItem, User commenter) {
        return Arrays.asList(
                Comment.builder().id(1L).text("Test Comment 1").item(commentedItem).author(commenter).build(),
                Comment.builder().id(2L).text("Test Comment 2").item(commentedItem).author(commenter).build()
        );
    }

    private void setupGetByIdMocks(Item item, ItemDto itemDto, List<Booking> bookings, List<Comment> comments) {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);
        when(bookingRepository.findAllByItemId(item.getId())).thenReturn(bookings);
        when(commentRepository.findAllByItemId(item.getId())).thenReturn(comments);
    }
}
