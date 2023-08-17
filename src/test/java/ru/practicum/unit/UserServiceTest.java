package ru.practicum.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserRepositoryJpa;
import ru.practicum.user.UserService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepositoryJpa repository;

    @Test
    public void testCreate() {
        UserDto userDto = UserDto.builder()
                .email("test@example.com")
                .build();
        when(repository.save(any(User.class))).thenReturn(new User());
        userService.create(userDto);
        verify(repository).save(any(User.class));
    }

    @Test(expected = BadRequestException.class)
    public void testCreateWithNullEmail() {
        UserDto userDto = UserDto.builder().build();
        userService.create(userDto);
    }

    @Test
    public void testGet() {
        when(repository.findById(1L)).thenReturn(Optional.of(new User()));
        userService.get(1L);
        verify(repository).findById(1L);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        userService.get(1L);
    }

    @Test
    public void testUpdate() {
        UserDto userDto = UserDto.builder()
                .email("new@example.com")
                .name("New Name")
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(new User()));
        when(repository.save(any(User.class))).thenReturn(new User());
        userService.update(1L, userDto);
        verify(repository).findById(1L);
        verify(repository).save(any(User.class));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotFound() {
        UserDto userDto = UserDto.builder().build();
        when(repository.findById(1L)).thenReturn(Optional.empty());
        userService.update(1L, userDto);
    }

    @Test
    public void testRemove() {
        when(repository.findById(1L)).thenReturn(Optional.of(new User()));
        userService.remove(1L);
        verify(repository).findById(1L);
        verify(repository).delete(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        when(repository.findAll()).thenReturn(Collections.singletonList(new User()));
        userService.getAllUsers();
        verify(repository).findAll();
    }

    @Test
    public void testGetUserDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(new User()));
        userService.getUser(1L);
        verify(repository).findById(1L);
    }
}
