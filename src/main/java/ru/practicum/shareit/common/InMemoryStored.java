package ru.practicum.shareit.common;

import org.springframework.lang.NonNull;

public interface InMemoryStored<T> {
    T copyOf();
    Long getId();
    void setId(@NonNull Long id);
}
