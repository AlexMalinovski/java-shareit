package ru.practicum.shareit.booking.enums;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.Getter;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.booking.model.QBooking;

import java.time.LocalDateTime;

@Getter
public enum StateFilter {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    UNSUPPORTED;

    public BooleanExpression getExpression(@NonNull LocalDateTime onTime) {
        if (this == ALL) {
            return Expressions.TRUE.isTrue();
        } else if (this == CURRENT) {
            return QBooking.booking.start.loe(onTime)
                    .and(QBooking.booking.end.goe(onTime));

        } else if (this == PAST) {
            return QBooking.booking.end.lt(onTime);

        } else if (this == FUTURE) {
            return QBooking.booking.start.gt(onTime);

        } else if (this == WAITING) {
            return QBooking.booking.status.eq(BookStatus.WAITING);

        } else if (this == REJECTED) {
            return QBooking.booking.status.eq(BookStatus.REJECTED);

        } else return Expressions.FALSE.isFalse();
    }
}
