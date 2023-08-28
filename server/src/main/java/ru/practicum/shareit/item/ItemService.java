package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepositoryJpa;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequestRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserRepositoryJpa userRepository;
    private final ItemRepositoryJpa itemRepository;
    private final BookingRepositoryJpa bookingRepository;
    private final CommentRepositoryJpa commentRepository;
    private final ItemRequestRepositoryJpa itemRequestRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    public Item get(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(""));
    }

    public ItemDto getById(Long itemId, Long userId) {
        Item item = get(itemId);
        ItemDto itemDto = itemMapper.toItemDto(item);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            addBookings(itemDto, bookingRepository.findAllByItemId(itemDto.getId()));
        }
        addCommentsDto(itemDto);
        return itemDto;
    }

    public ItemDto add(Long userId, ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(getUser(userId));
        Long requestId = itemDto.getRequestId();
        if (requestId != null) {
            item.setRequest(itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("")));
        }
        Item item1 = itemRepository.save(item);
        return itemMapper.toItemDto(item1);
    }

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = get(itemId);
        User user = getUser(userId);
        if (itemDto.getOwner() != null && !Objects.equals(itemDto.getOwner().getId(), userId)) {
            throw new ForbiddenException("");
        }
        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);

        item.setOwner(user);
        itemRepository.save(item);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    public List<ItemDto> search(String text) {
        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase())
                        && item.getAvailable().equals(true))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> getAll(Long userId) {
        List<Booking> bookingList = bookingRepository.findAll();
        List<Item> allByOwner = itemRepository.findAllByOwner(getUser(userId));
        return allByOwner.stream()
                .sorted((x, y) -> Math.toIntExact(x.getId() - y.getId()))
                .map(x -> {
                    ItemDto itemDto = itemMapper.toItemDto(x);
                    List<Booking> bookings = bookingList.stream().filter(y -> y.getItem().getId().equals(itemDto.getId())).collect(Collectors.toList());
                    addBookings(itemDto, bookings);
                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        if (isBookingExists(itemId, userId)) {
            throw new BadRequestException("isBookingExists(itemId, userId):" + itemId + " " + userId);
        }
        if (commentDto.getText().isBlank()) {
            throw new BadRequestException("commentDto.getText().isBlank()");
        }
        Item item = get(itemId);
        User user = getUser(userId);
        Comment comment = commentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        Comment save = commentRepository.save(comment);
        return commentMapper.toCommentDto(save);
    }

    private boolean isBookingExists(long itemId, long userId) {
        return bookingRepository.findAllByItemIdAndBookerId(itemId, userId).stream()
                .noneMatch(booking -> booking.getStatus() == Status.APPROVED
                        && booking.getStart().isBefore(LocalDateTime.now()));
    }

    private void addCommentsDto(ItemDto itemDto) {
        List<Comment> comments = commentRepository.findAllByItemId(itemDto.getId());
        itemDto.setComments(comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList()));
    }

    private void addBookings(ItemDto itemDto, List<Booking> bookings) {
        Booking prevBooking = bookings.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null);

        Booking nextBooking = bookings.stream()
                .filter(b -> b.getStatus().equals(Status.APPROVED))
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);

        itemDto.setLastBooking(prevBooking != null ? ItemDto.NearByBooking.builder()
                .id(prevBooking.getId())
                .bookerId(prevBooking.getBooker().getId())
                .build() : null);

        itemDto.setNextBooking(nextBooking != null ? ItemDto.NearByBooking.builder()
                .id(nextBooking.getId())
                .bookerId(nextBooking.getBooker().getId())
                .build() : null);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(""));
    }
}
