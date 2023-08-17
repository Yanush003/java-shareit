package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepositoryJpa;
import ru.practicum.booking.Status;
import ru.practicum.comment.Comment;
import ru.practicum.comment.CommentDto;
import ru.practicum.comment.CommentMapper;
import ru.practicum.comment.CommentRepositoryJpa;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserService userService;
    private final ItemRepositoryJpa itemRepository;
    private final BookingRepositoryJpa bookingRepository;
    private final CommentRepositoryJpa commentRepository;

    public Item get(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(""));
    }

    public ItemDto getById(Long itemId, Long userId) {
        Item item = get(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            addBookings(itemDto);
        }
        addCommentsDto(itemDto);
        return itemDto;
    }

    public ItemDto add(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userService.get(userId));
        Item item1 = itemRepository.save(item);
        return ItemMapper.toItemDto(item1);
    }

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = get(itemId);
        User user = userService.get(userId);
        if (itemDto.getOwner() != null && !Objects.equals(itemDto.getOwner().getId(), userId)) {
            throw new ForbiddenException("");
        }
        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);

        item.setOwner(user);
        itemRepository.save(item);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public List<ItemDto> search(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase())
                        && item.getAvailable().equals(true))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> getAll(Long userId) {
        List<Item> allByOwner = itemRepository.findAllByOwner(userService.get(userId));
        return allByOwner.stream()
                .sorted((x, y) -> Math.toIntExact(x.getId() - y.getId()))
                .map(x -> {
                    ItemDto itemDto = ItemMapper.toItemDto(x);
                    addBookings(itemDto);
                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        if (isBookingExists(itemId, userId)) {
            throw new BadRequestException("");
        }
        if (commentDto.getText().isBlank()) {
            throw new BadRequestException("");
        }
        Item item = get(itemId);
        User user = userService.get(userId);
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        Comment save = commentRepository.save(comment);
        return CommentMapper.toCommentDto(save);
    }

    private boolean isBookingExists(long itemId, long userId) {
        return bookingRepository.findAllByItemIdAndBookerId(itemId, userId).stream()
                .noneMatch(booking -> booking.getStatus() == Status.APPROVED
                        && booking.getStart().isBefore(LocalDateTime.now()));
    }

    private void addCommentsDto(ItemDto itemDto) {
        var comments = commentRepository.findAllByItemId(itemDto.getId());
        itemDto.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
    }

    private void addBookings(ItemDto itemDto) {
        List<Booking> bookings = bookingRepository.findAllByItemId(itemDto.getId());

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
}
