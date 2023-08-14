package ru.practicum.item;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.booking.BookingRepositoryJpa;
import ru.practicum.comment.CommentRepositoryJpa;
import ru.practicum.request.ItemRequestRepositoryJpa;
import ru.practicum.user.UserService;

public class ItemServiceTest {

    private UserService userService = Mockito.mock(UserService.class);
    private ItemRepositoryJpa itemRepository = Mockito.mock(ItemRepositoryJpa.class);
    private BookingRepositoryJpa bookingRepository = Mockito.mock(BookingRepositoryJpa.class);
    private CommentRepositoryJpa commentRepository = Mockito.mock(CommentRepositoryJpa.class);
    private ItemRequestRepositoryJpa itemRequestRepository = Mockito.mock(ItemRequestRepositoryJpa.class);

    private ItemService itemService = new ItemService(userService, itemRepository, bookingRepository, commentRepository, itemRequestRepository);


    @Test
    void getByIdTest() {

    }

    @Test
    void addTest(){

    }

    @Test
    void updateTest(){

    }

    @Test
    void searchTest(){

    }

    @Test
    void getAllTest(){

    }

    @Test
    void addCommentTest(){

    }

    @Test
    void addBookingsTest(){

    }
}