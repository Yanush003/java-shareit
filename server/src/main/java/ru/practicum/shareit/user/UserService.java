package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryJpa repository;
    private final UserMapper userMapper;

    public UserDto create(UserDto userDto) {

        User user = userMapper.toUser(userDto);
        User user1 = repository.save(user);
        return userMapper.toUserDto(user1);
    }

    public User get(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(""));
    }

    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        userDto.setId(id);
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException(""));
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        User update = repository.save(user);
        return userMapper.toUserDto(update);
    }

    public void remove(Long userId) {
        repository.delete(get(userId));
    }

    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getUser(Long userId) {
        return userMapper.toUserDto(get(userId));
    }
}
