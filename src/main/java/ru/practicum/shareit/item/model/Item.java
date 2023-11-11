package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.common.InMemoryStored;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Data
@Builder
public class Item implements InMemoryStored<Item> {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;

    @Override
    public Item copyOf() {
        return Item.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .available(this.available)
                .owner(this.owner != null ? this.owner.copyOf() : null)
                .build();
    }

    public boolean isOwner(final long id) {
        return Optional.ofNullable(this.owner)
                .map(User::getId)
                .map(ownId -> id == ownId)
                .orElse(false);
    }

    public Long getOwnerId() {
        return Optional.ofNullable(this.owner)
                .map(User::getId)
                .orElse(null);
    }

    public boolean isContainSubstring(@NonNull final String substr) {
        return findSubstringIgnoreCase(this.name, substr)
                || findSubstringIgnoreCase(this.description, substr);
    }

    private boolean findSubstringIgnoreCase(final String string, String substring) {
        final String searchBy = substring.toLowerCase();
        return Optional.ofNullable(string)
                .map(String::toLowerCase)
                .map(s -> s.contains(searchBy))
                .orElse(false);
    }

    public Item updateOnNonNullFields(@NonNull Item itemUpdates) {
        Optional.ofNullable(itemUpdates.getName())
                .ifPresent(this::setName);
        Optional.ofNullable(itemUpdates.getDescription())
                .ifPresent(this::setDescription);
        Optional.ofNullable(itemUpdates.getAvailable())
                .ifPresent(this::setAvailable);
        return this;
    }
}
