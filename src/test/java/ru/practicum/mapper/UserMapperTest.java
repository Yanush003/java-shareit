package ru.practicum.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.user.User;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    public void testToUserDto() {
        User user = createTestUser();
        UserDto userDto = userMapper.toUserDto(user);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    public void testToUser() {
        UserDto userDto = createTestUserDto();
        User user = userMapper.toUser(userDto);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        return user;
    }

    private UserDto createTestUserDto() {
        return UserDto.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
    }
}
