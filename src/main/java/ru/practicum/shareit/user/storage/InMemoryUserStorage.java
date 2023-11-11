package ru.practicum.shareit.user.storage;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.InMemoryStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage {

    @Override
    public User createUser(@NonNull User user) {
        return create(user);
    }

    @Override
    public Optional<User> getUserById(long id) {
        return getById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return getAll();
    }

    @Override
    public User updateUser(@NonNull User user) {
        return update(user);
    }

    @Override
    public Optional<User> getUserByEmail(@NonNull final String email) {
        return objects.values()
                .stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findAny();
    }

    @Override
    public void deleteUser(long id) {
        deleteById(id);
    }

    @Override
    public boolean isUserEmailNotFound(@NonNull final String email) {
        return objects.values()
                .stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findAny()
                .isEmpty();
    }
}
