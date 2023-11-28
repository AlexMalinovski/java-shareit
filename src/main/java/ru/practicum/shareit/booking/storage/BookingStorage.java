package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking>,
        BookingStorageCustom {

}
