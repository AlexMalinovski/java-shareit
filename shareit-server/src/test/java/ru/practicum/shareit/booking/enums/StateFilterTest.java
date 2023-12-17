package ru.practicum.shareit.booking.enums;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.StateFilter;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StateFilterTest {

    @Test
    void getExpression_ifInvoked_thenReturnExpression() {
        LocalDateTime onDate = LocalDateTime.now();
        for (StateFilter stateFilter : StateFilter.values()) {
            BooleanExpression actual = stateFilter.getExpression(onDate);
            assertNotNull(actual);
        }
    }

    @Test
    void getExpression_ifUnsupported_thenReturnFalseExpression() {
        LocalDateTime onDate = LocalDateTime.now();

        BooleanExpression actual = StateFilter.UNSUPPORTED.getExpression(onDate);

        assertEquals(Expressions.FALSE.isFalse(), actual);
    }
}