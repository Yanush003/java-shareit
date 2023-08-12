package ru.practicum.item;

import lombok.Builder;
import lombok.Data;
import ru.practicum.comment.CommentDto;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ItemDto {
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private User owner;

    private ItemRequest request;

    private Long requestId;

    private NearByBooking lastBooking;

    private NearByBooking nextBooking;

    private List<CommentDto> comments;

    @Data
    @Builder
    public static class NearByBooking {
        private Long id;
        private Long bookerId;
    }
}
