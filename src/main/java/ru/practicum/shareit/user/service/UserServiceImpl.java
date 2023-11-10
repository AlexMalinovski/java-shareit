package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    @NonNull
    public User createUser(@NonNull User user) {
        final String email = user.getEmail();
        if (userStorage.isUserEmailNotFound(email)) {
            return userStorage.createUser(user);
        }
        throw new ConflictException(String.format("Пользователь с email=%s уже существует", user.getEmail()));
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userStorage.getUserById(id);
    }

    @Override
    @NonNull
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public Optional<User> deleteUser(long id) {
        return userStorage
                .getUserById(id)
                .map(u -> {
                    userStorage.deleteUser(id);
                    return u;
                });
    }

    @Override
    @NonNull
    public User updateUser(@NonNull User user) {
        final Long userId = user.getId();
        final boolean isConflictEmail = Optional.ofNullable(user.getEmail())
                .flatMap(userStorage::getUserByEmail)
                .map(User::getId)
                .map(id -> !userId.equals(id))
                .orElse(false);
        if (isConflictEmail) {
            throw new ConflictException(String.format("Пользователь с email=%s уже существует", user.getEmail()));
        }
        return userStorage.getUserById(userId)
                .map(u -> u.updateOnNonNullFields(user))
                .map(userStorage::updateUser)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
    }
}
