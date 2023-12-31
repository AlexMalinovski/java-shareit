package ru.practicum.shareit.booking.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.library.api.booking.dto.BookingDto;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.BookStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring",
        imports = {User.class, Item.class, BookStatus.class},
        uses = {UserMapper.class, ItemMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookingMapper {

    @Mapping(target = "status", expression = "java(BookStatus.WAITING)")
    @Mapping(target = "item", expression = "java(Item.builder().id(dto.getItemId()).build())")
    @Mapping(target = "start", source = "dto.start", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "end", source = "dto.end", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    Booking mapCreateBookingDtoToBooking(CreateBookingDto dto);

    @Mapping(target = "start", source = "booking.start", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "end", source = "booking.end", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    BookingDto mapBookingToBookingDto(Booking booking);

    List<BookingDto> mapBookingToBookingDto(List<Booking> bookings);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "booker", expression = "java(User.builder().id(src.getBooker().getId()).build())")
    @Mapping(target = "item", expression = "java(Item.builder().id(src.getItem().getId()).build())")
    @Mapping(target = "status", expression = "java(BookStatus.WAITING)")
    @Mapping(target = "start", source = "src.start")
    @Mapping(target = "end", source = "src.end")
    Booking mapBookingToNewBooking(Booking src);
}
