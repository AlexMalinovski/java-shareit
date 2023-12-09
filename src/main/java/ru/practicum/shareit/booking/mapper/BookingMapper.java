package ru.practicum.shareit.booking.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, ItemMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookingMapper {

    @Mapping(target = "status", expression = "java(ru.practicum.shareit.booking.enums.BookStatus.WAITING)")
    @Mapping(target = "item", expression = "java(ru.practicum.shareit.item.model.Item.builder().id(dto.getItemId()).build())")
    @Mapping(target = "start", source = "dto.start", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "end", source = "dto.end", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    Booking mapCreateBookingDtoToBooking(CreateBookingDto dto);

    @Mapping(target = "start", source = "booking.start", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "end", source = "booking.end", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    BookingDto mapBookingToBookingDto(Booking booking);

    List<BookingDto> mapBookingToBookingDto(List<Booking> bookings);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "booker", expression = "java(ru.practicum.shareit.user.model.User.builder().id(src.getBooker().getId()).build())")
    @Mapping(target = "item", expression = "java(ru.practicum.shareit.item.model.Item.builder().id(src.getItem().getId()).build())")
    @Mapping(target = "status", expression = "java(ru.practicum.shareit.booking.enums.BookStatus.WAITING)")
    @Mapping(target = "start", source = "src.start")
    @Mapping(target = "end", source = "src.end")
    Booking mapBookingToNewBooking(Booking src);
}
