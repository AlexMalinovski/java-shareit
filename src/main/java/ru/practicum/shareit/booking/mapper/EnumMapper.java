package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.enums.StateFilter;

@Mapper(componentModel = "spring")
public abstract class EnumMapper {
    public StateFilter mapStringToStateFilter(String string) {
        if (string == null || string.isBlank()) {
            return StateFilter.UNSUPPORTED;
        }
        try {
            return StateFilter.valueOf(string.toUpperCase());
        } catch (Exception ex) {
            return StateFilter.UNSUPPORTED;
        }
    }
}
