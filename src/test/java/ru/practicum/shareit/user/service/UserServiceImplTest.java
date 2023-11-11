package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
        when(userStorage.isUserEmailNotFound(anyString())).thenReturn(false);

        assertThrows(ConflictException.class, () -> userService.createUser(expected));

        verify(userStorage).isUserEmailNotFound(expected.getEmail());
    }

    @Test
    void createUser_returnCreated() {
        var expected = getValidNewUser();
        when(userStorage.isUserEmailNotFound(expected.getEmail())).thenReturn(true);
        when(userStorage.createUser(expected)).thenReturn(getValidUser());

        var actual = userService.createUser(expected);

        verify(userStorage).isUserEmailNotFound(expected.getEmail());
        verify(userStorage).createUser(expected);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void getUserById_ifNotFound_thenReturnEmpty() {
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.empty());

        var actual = userService.getUserById(1L);

        verify(userStorage).getUserById(1L);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void getUserById_ifFound_thenReturnFounded() {
        var expected = getValidUser();
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.of(expected));

        var actual = userService.getUserById(1L);

        verify(userStorage).getUserById(1L);
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getAllUsers_ifNotFound_thenReturnEmptyList() {
        when(userStorage.getAllUsers()).thenReturn(List.of());

        var actual = userService.getAllUsers();

        verify(userStorage).getAllUsers();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void deleteUser_ifFound_thenReturnFoundedList() {
        var expected = getValidUser();
        when(userStorage.getAllUsers()).thenReturn(List.of(expected));

        var actual = userService.getAllUsers();

        verify(userStorage).getAllUsers();
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    void updateUser_ifConflictEmail_thenThrowConflictException() {
        var expected = getValidUser();
        when(userStorage.getUserByEmail(expected.getEmail()))
                .thenReturn(Optional.of(User.builder().id(100L).email(expected.getEmail()).build()));

        assertThrows(ConflictException.class, () -> userService.updateUser(expected));

        verify(userStorage).getUserByEmail(expected.getEmail());
    }

    @Test
    void updateUser_ifUserNotFound_thenThrowNotFoundException() {
        var expected = getValidUser();
        when(userStorage.getUserByEmail(expected.getEmail())).thenReturn(Optional.empty());
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(expected));

        verify(userStorage).getUserByEmail(expected.getEmail());
        verify(userStorage).getUserById(expected.getId());
    }

    @Test
    void updateUser_returnUpdated() {
        var oldUser = getValidUser();
        User updates = User.builder().id(oldUser.getId()).name("new name").email("new@e.mail").build();
        User expected = oldUser.copyOf();
        expected.setName(updates.getName());
        expected.setEmail(updates.getEmail());
        when(userStorage.getUserByEmail(updates.getEmail())).thenReturn(Optional.empty());
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.of(oldUser));
        when(userStorage.updateUser(updates)).thenReturn(expected);

        var actual = userService.updateUser(updates);

        verify(userStorage).getUserByEmail(updates.getEmail());
        verify(userStorage).getUserById(updates.getId());
        verify(userStorage).updateUser(updates);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}