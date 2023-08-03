package ru.practicum.user;

import org.springframework.stereotype.Repository;
import ru.practicum.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    private static Long id = 1L;

    @Override
    public User create(User user) {
        if (validateEmail(user)) {
            throw new DuplicateEmailException(user.getEmail());
        }
        user.setId(id);
        users.put(id, user);
        id++;
        return user;
    }

    @Override
    public User get(Long userId) {
        if (users.get(userId) == null) {
            throw new NotFoundException("");
        }
        return users.get(userId);
    }

    @Override
    public User update(Long userId, User user) {
        User oldUser = users.get(userId);
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            if (searchUserByEmail(user.getEmail(), userId).isPresent()) {
                throw new DuplicateEmailException("");
            }
            oldUser.setEmail(user.getEmail());
        }
        return users.put(oldUser.getId(), oldUser);
    }

    @Override
    public void remove(Long userId) {
        users.remove(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private Optional<User> searchUserByEmail(String email, Long userId) {
        return users.values()
                .stream()
                .filter(x -> !x.getId().equals(userId))
                .filter(x -> x.getEmail().equals(email))
                .findFirst();
    }

    private boolean validateEmail(User user) {
        List<User> userList = users.values()
                .stream()
                .filter(x -> x.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList());
        return userList.size() > 0;
    }
}
