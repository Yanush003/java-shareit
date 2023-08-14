package ru.practicum.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserServiceTest {

    private UserRepositoryJpa repository = Mockito.mock(UserRepositoryJpa.class);

    private UserService userService = new UserService(repository);

    @Test
    void createTest() {

    }

    @Test
    void updateTest() {

    }

    @Test
    void getAllUsersTest() {

    }
}
