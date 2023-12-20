package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;
import ru.practicum.shareit.library.api.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapper mapper = new UserMapperImpl();

    @Test
    void mapCreateUserDtoToUser_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapCreateUserDtoToUser(null);
        assertNull(actual);
    }

    @Test
    void mapCreateUserDtoToUser() {
        CreateUserDto expected = CreateUserDto.builder().name("name").email("e@mail.ru").build();

        User actual = mapper.mapCreateUserDtoToUser(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertNull(actual.getId());
    }

    @Test
    void mapUpdateUserDtoToUser_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapUpdateUserDtoToUser(null);
        assertNull(actual);
    }

    @Test
    void mapUpdateUserDtoToUser() {
        UpdateUserDto expected = UpdateUserDto.builder().name("name").email("e@mail.ru").build();

        User actual = mapper.mapUpdateUserDtoToUser(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertNull(actual.getId());
    }

    @Test
    void mapUserToUserDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapUserToUserDto((User) null);
        assertNull(actual);
    }

    @Test
    void mapUserToUserDto() {
        User expected = User.builder().id(1L).name("name").email("e@mail.ru").build();

        UserDto actual = mapper.mapUserToUserDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }
}