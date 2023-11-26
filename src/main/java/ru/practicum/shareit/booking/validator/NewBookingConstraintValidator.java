package ru.practicum.shareit.booking.validator;

import ru.practicum.shareit.booking.model.Booking;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class NewBookingConstraintValidator implements ConstraintValidator<NewBooking, Booking> {

    @Override
    public boolean isValid(Booking booking, ConstraintValidatorContext context) {
        if (booking == null) {
            return true;
        }
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        Consumer<String> addMessage = s -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(s)
                    .addConstraintViolation();
        };

        if (start == null) {
            addMessage.accept("Не установлена дата начала бронирования");
            return false;
        }
        if (end == null) {
            addMessage.accept("Не установлена дата завершения бронирования");
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        if (start.isBefore(now)) {
            addMessage.accept("Дата начала бронирования раньше текущей даты");
            return false;
        }
        if (end.isBefore(now)) {
            addMessage.accept("Дата завершения бронирования раньше текущей даты");
            return false;
        }
        if (start.isAfter(end)) {
           addMessage.accept("Дата начала бронирования позже даты завершения");
           return false;
        }
        if (start.isEqual(end)) {
            addMessage.accept("Дата начала бронирования равна дате завершения");
            return false;
        }
        return true;
    }
}
