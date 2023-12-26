package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.StateFilter;
import ru.practicum.shareit.booking.mapper.EnumMapper;
import ru.practicum.shareit.booking.mapper.EnumMapperImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EnumMapperTest {

    private final EnumMapper enumMapper = new EnumMapperImpl();

    @Test
    void mapStringToStateFilter_ifStringIsValidLowCase_thenMap() {
        for (StateFilter expected : StateFilter.values()) {

            StateFilter actual = enumMapper.mapStringToStateFilter(expected.name().toLowerCase());

            assertNotNull(actual);
            assertEquals(expected, actual);
        }
    }

    @Test
    void mapStringToStateFilter_ifStringIsValidUpCase_thenMap() {
        for (StateFilter expected : StateFilter.values()) {

            StateFilter actual = enumMapper.mapStringToStateFilter(expected.name().toUpperCase());

            assertNotNull(actual);
            assertEquals(expected, actual);
        }
    }

    @Test
    void mapStringToStateFilter_ifStringNotValidUpCase_thenMapToUnsupported() {
        StateFilter actual = enumMapper.mapStringToStateFilter("qwerty");

        assertNotNull(actual);
        assertEquals(StateFilter.UNSUPPORTED, actual);
    }

    @Test
    void mapStringToStateFilter_ifStringIsNull_thenMapToUnsupported() {
        StateFilter actual = enumMapper.mapStringToStateFilter(null);

        assertNotNull(actual);
        assertEquals(StateFilter.UNSUPPORTED, actual);
    }

    @Test
    void mapStringToStateFilter_ifStringIsEmpty_thenMapToUnsupported() {
        StateFilter actual = enumMapper.mapStringToStateFilter("");

        assertNotNull(actual);
        assertEquals(StateFilter.UNSUPPORTED, actual);
    }
}