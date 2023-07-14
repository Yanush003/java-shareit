package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateEmailException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryImpl repository;

    public UserDto create(UserDto userDto) {
        if (userDto.getEmail()==null){
            throw new ValidateEmailException("");
        }
        User user = UserMapper.toUser(userDto);
        User user1 = repository.create(user);
        return UserMapper.toUserDto(user1);
    }

    public User get(Long userId) {
        return Optional.ofNullable(repository.get(userId))
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not exist"));
    }

    public UserDto update(Long id, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User update = repository.update(id, user);
        return UserMapper.toUserDto(update);
    }

    public void remove(Long userId) {
        repository.remove(userId);
    }

    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
