package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceImplItTest {

    @Autowired
    UserService userService;

    @Test
    @Sql("/user-service-it-test.sql")
    void updateUser_ifInvoke_thenUpdate() {
        User expected = User.builder()
                .id(1L)
                .name("ownerNev")
                .build();

        userService.updateUser(expected);
        var actual = userService.getUserById(1L);

        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(expected.getName(), actual.get().getName());
    }

    @Test
    @Sql("/user-service-it-test.sql")
    void updateUser_ifEmailDuplicate_thenDataIntegrityViolationException() {
        User expected = User.builder()
                .id(1L)
                .email("uSer@mail.ru")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userService.updateUser(expected));
    }
}