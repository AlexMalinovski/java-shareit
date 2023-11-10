package ru.practicum.shareit.common;

import org.springframework.lang.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public abstract class InMemoryStorage<T extends InMemoryStored<T>> {
    protected final Map<Long, T> objects;
    private final AtomicLong currentId;

    public InMemoryStorage() {
        objects = new ConcurrentHashMap<>();
        currentId = new AtomicLong(1L);
    }

    protected T create(@NonNull T obj) {
        final T objCopy = obj.copyOf();
        objCopy.setId(currentId.getAndIncrement());
        objects.put(objCopy.getId(), objCopy);
        return objCopy.copyOf();
    }

    protected T update(@NonNull T obj) {
        return Optional.ofNullable(objects.computeIfPresent(obj.getId(), (k, v) -> obj.copyOf()))
                .map(InMemoryStored::copyOf)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Объект с id=%d отсутствует в хранилище", obj.getId())
                ));
    }

    protected Optional<T> getById(final long id) {
        return Optional.ofNullable(objects.get(id))
                .map(InMemoryStored::copyOf);
    }

    protected List<T> getAll() {
        return objects.values()
                .stream()
                .sorted(Comparator.comparingLong(InMemoryStored::getId))
                .map(InMemoryStored::copyOf)
                .collect(Collectors.toUnmodifiableList());
    }

    protected void deleteById(long id) {
        objects.remove(id);
    }
}
