package ru.practicum.shareit.request.service;

import org.springframework.lang.NonNull;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestService {

    @NonNull
    ItemRequest createItemRequest(@NonNull ItemRequest itemRequest, long userId);

    List<ItemRequest> getOwnedItemRequests(long ownerId);

    List<ItemRequest> getItemRequests(long userId, int from, int size);

    Optional<ItemRequest> getItemRequestById(long requestId, long userId);

}
