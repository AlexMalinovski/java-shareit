package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        try {
            return userStorage.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(String.format("Пользователь с email=%s уже существует", user.getEmail()));
        }
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userStorage.findById(id);
    }

    @Override
    @NonNull
    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    @Override
    @Transactional
    public Optional<User> deleteUser(long id) {
        return userStorage.findById(id)
                .map(u -> {
                    userStorage.deleteById(id);
                    return u;
                });
    }

    @Override
    @NonNull
    @Transactional
    public User updateUser(@NonNull User user) {
        try {
            final Long userId = user.getId();
            return userStorage.findById(userId)
                    .map(foundedUser -> foundedUser.toBuilder()
                            .name(user.getName() != null ? user.getName() : foundedUser.getName())
                            .email(user.getEmail() != null ? user.getEmail() : foundedUser.getEmail())
                            .build())
                    .map(userStorage::save)
                    .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(String.format("Пользователь с email=%s уже существует", user.getEmail()));
        }
    }
}
