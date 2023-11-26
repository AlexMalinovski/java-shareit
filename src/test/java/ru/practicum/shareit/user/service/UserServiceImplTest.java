package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private UserServiceImpl userService;

    private User getValidNewUser() {
        return User.builder()
                .name("name")
                .email("name@e.mail")
                .build();
    }

    private User getValidUser() {
        return User.builder()
                .id(1L)
                .name("name")
                .email("name@e.mail")
                .build();
    }

    @Test
    void createUser_ifConflictEmail_thenThrowConflictException() {
        var expected = getValidUser();
        when(userStorage.save(any(User.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ConflictException.class, () -> userService.createUser(expected));

        verify(userStorage).save(expected);
    }

    @Test
    void createUser_returnCreated() {
        var expected = getValidNewUser();
        when(userStorage.save(expected)).thenReturn(getValidUser());

        var actual = userService.createUser(expected);

        verify(userStorage).save(expected);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void getUserById_ifNotFound_thenReturnEmpty() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.empty());

        var actual = userService.getUserById(1L);

        verify(userStorage).findById(1L);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void getUserById_ifFound_thenReturnFounded() {
        var expected = getValidUser();
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(expected));

        var actual = userService.getUserById(1L);

        verify(userStorage).findById(1L);
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getAllUsers_ifNotFound_thenReturnEmptyList() {
        when(userStorage.findAll()).thenReturn(List.of());

        var actual = userService.getAllUsers();

        verify(userStorage).findAll();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void deleteUser_ifFound_thenReturnFoundedList() {
        var expected = getValidUser();
        when(userStorage.findAll()).thenReturn(List.of(expected));

        var actual = userService.getAllUsers();

        verify(userStorage).findAll();
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    void updateUser_ifConflictEmail_thenThrowConflictException() {
        var expected = getValidUser();
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(expected));
        when(userStorage.save(any(User.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ConflictException.class, () -> userService.updateUser(expected));

        verify(userStorage).findById(expected.getId());
        verify(userStorage).save(expected);
    }

    @Test
    void updateUser_ifUserNotFound_thenThrowNotFoundException() {
        var expected = getValidUser();
        when(userStorage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(expected));

        verify(userStorage).findById(expected.getId());
    }

    @Test
    void updateUser_returnUpdated() {
        var oldUser = getValidUser();
        User updates = User.builder().id(oldUser.getId()).name("new name").email("new@e.mail").build();
        User expected = getValidUser().toBuilder().name(updates.getName()).email(updates.getEmail()).build();
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(oldUser));
        when(userStorage.save(updates)).thenReturn(expected);

        var actual = userService.updateUser(updates);

        verify(userStorage).findById(updates.getId());
        verify(userStorage).save(updates);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}