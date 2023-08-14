package ru.practicum.request;

import org.mockito.Mockito;
import ru.practicum.user.UserService;

public class ItemRequestServiceTest {

    private final ItemRequestRepositoryJpa itemRequestRepository = Mockito.mock(ItemRequestRepositoryJpa.class);
    private final UserService userService= Mockito.mock(UserService.class);

    ItemRequestService itemRequestService = new ItemRequestService(itemRequestRepository, userService);
}
