package ru.practicum.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    public User toUser(UserDto userDto) {
        return User.builder()
                .id((userDto.getId() == null) ? null : userDto.getId())
                .name((userDto.getName() == null) ? null : userDto.getName())
                .email((userDto.getEmail() == null) ? null : userDto.getEmail())
                .build();
    }
}
