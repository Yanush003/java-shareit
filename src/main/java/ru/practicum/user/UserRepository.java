package ru.practicum.user;

import java.util.List;

interface UserRepository {
    User create(User user);

    User get(Long userId);

    User update(Long userId, User user);

    void remove(Long userId);

    List<User> findAll();
}
