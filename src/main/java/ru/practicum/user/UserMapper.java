package ru.practicum.user;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id((userDto.getId() == null) ? null : userDto.getId())
                .name((userDto.getName() == null) ? null : userDto.getName())
                .email((userDto.getEmail() == null) ? null : userDto.getEmail())
                .build();
    }

}
