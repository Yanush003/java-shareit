package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateEmailException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryJpa repository;

    public UserDto create(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidateEmailException("");
        }
        User user = UserMapper.toUser(userDto);
        User user1 = repository.save(user);
        return UserMapper.toUserDto(user1);
    }

    public User get(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not exist"));
    }

    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        userDto.setId(id);
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("User with id=" + id + " not exist"));

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        User update = repository.save(user);
        return UserMapper.toUserDto(update);
    }

    public void remove(Long userId) {
        repository.delete(get(userId));
    }

    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    private boolean validateEmail(User user) {
        List<User> userList = repository.findAll()
                .stream()
                .filter(x -> x.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList());
        return userList.size() > 0;
    }
}
