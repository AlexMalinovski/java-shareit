package ru.practicum.shareit.gateway.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserClientImplItTest {

    @Autowired
    private UserClient userClient;

    @Test
    void createUser_ifInvalidDto_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().email("a@mail.ru").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().name("").email("a@mail.ru").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().name(" ").email("a@mail.ru").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().name("a".repeat(256)).email("a@mail.ru").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().name("name").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().email("").name("name").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().email(" ").name("name").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().email("email").name("name").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().email("email.ru").name("name").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.createUser(
                CreateUserDto.builder().email("e@m." + "a".repeat(509)).name("name").build()));

    }

    @Test
    void getUserById_ifInvalidId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> userClient.getUserById(-1L));
        assertThrows(ConstraintViolationException.class, () -> userClient.getUserById(0L));
    }

    @Test
    void updateUser_ifInvalidId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> userClient.updateUser(
                -1L, UpdateUserDto.builder().name("name").email("e@mail.ru").build()));
        assertThrows(ConstraintViolationException.class, () -> userClient.updateUser(
                0L, UpdateUserDto.builder().name("name").email("e@mail.ru").build()));
    }

    @Test
    void updateUser_ifInvalidDto_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> userClient.updateUser(
                1L, UpdateUserDto.builder().name("").email("e@mail.ru").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.updateUser(
                1L, UpdateUserDto.builder().name("a".repeat(256)).email("a@mail.ru").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.updateUser(
                1L, UpdateUserDto.builder().name("  ").email("e@mail.ru").build()));

        assertThrows(ConstraintViolationException.class, () -> userClient.updateUser(
                1L, UpdateUserDto.builder().email("e@m." + "a".repeat(509)).name("name").build()));
    }

    @Test
    void deleteUser_ifInvalidId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> userClient.deleteUser(-1L));
        assertThrows(ConstraintViolationException.class, () -> userClient.deleteUser(0L));
    }
}