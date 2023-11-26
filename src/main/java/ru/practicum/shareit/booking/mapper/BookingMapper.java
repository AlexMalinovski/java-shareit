package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper {

    @Mapping(target = "status", expression = "java(ru.practicum.shareit.booking.enums.BookStatus.WAITING)")
    @Mapping(target = "item", expression = "java(ru.practicum.shareit.item.model.Item.builder().id(dto.getItemId()).build())")
    @Mapping(target = "start", source = "dto.start", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "end", source = "dto.end", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    Booking mapCreateBookingDtoToBooking(CreateBookingDto dto);

    @Mapping(target = "start", source = "booking.start", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "end", source = "booking.end", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    BookingDto mapBookingToBookingDto(Booking booking);
}
