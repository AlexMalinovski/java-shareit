package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.common.InMemoryStored;

import java.util.Optional;

@Data
@Builder
public class User implements InMemoryStored<User> {
    private Long id;
    private String name;
    private String email;

    @Override
    public User copyOf() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .build();
    }

    public User updateOnNonNullFields(User userUpdates) {
        Optional.ofNullable(userUpdates.getName())
                .ifPresent(this::setName);
        Optional.ofNullable(userUpdates.getEmail())
                .ifPresent(this::setEmail);
        return this;
    }
}
