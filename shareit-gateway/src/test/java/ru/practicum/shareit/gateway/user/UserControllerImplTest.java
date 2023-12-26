package ru.practicum.shareit.gateway.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.gateway.user.service.UserClient;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserControllerImpl.class)
class UserControllerImplTest {

    @MockBean
    private UserClient userClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void createUser() {
        CreateUserDto expected = CreateUserDto.builder().build();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(expected)))
                .andExpect(status().isOk());
        verify(userClient).createUser(expected);
    }

    @Test
    @SneakyThrows
    void getUserById() {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
        verify(userClient).getUserById(1L);
    }

    @Test
    @SneakyThrows
    void getAllUsers() {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
        verify(userClient).getAllUsers();
    }

    @Test
    @SneakyThrows
    void updateUser() {
        UpdateUserDto expected = UpdateUserDto.builder().build();
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(expected)))
                .andExpect(status().isOk());
        verify(userClient).updateUser(1L, expected);
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        verify(userClient).deleteUser(1L);
    }
}