package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapCreateUserDtoToUser(CreateUserDto createUserDto);

    User mapUpdateUserDtoToUser(UpdateUserDto updateUserDto);

    UserDto mapUserToUserDto(User user);
}
