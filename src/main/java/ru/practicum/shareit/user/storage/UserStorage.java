package ru.practicum.shareit.user.storage;

import org.springframework.lang.NonNull;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User createUser(@NonNull User user);

    Optional<User> getUserById(long id);

    List<User> getAllUsers();

    User updateUser(@NonNull User user);

    void deleteUser(long id);

    boolean isUserEmailNotFound(@NonNull String email);

    Optional<User> getUserByEmail(@NonNull String email);
}
