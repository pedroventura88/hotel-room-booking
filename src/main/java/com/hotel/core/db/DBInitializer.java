package com.hotel.core.db;

import com.hotel.core.reservation.Booking;
import com.hotel.core.reservation.BookingRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DBInitializer {

    private BookingRepository bookingRepository;

    public DBInitializer(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        populate();
    }

    private void populate() {
        Booking r1 = Booking.builder()
                .checkInDate(LocalDate.of(2022, 8, 18))
                .checkOutDate(LocalDate.of(2022, 8, 20))
                .isCanceled(false)
                .createTime(LocalDateTime.of(2022, 8, 15, 1, 1))
                .updateTime(LocalDateTime.of(2022, 8, 15, 1, 1))
                .reservedBy("customer1")
                .build();


        Booking r2 = Booking.builder()
                .checkInDate(LocalDate.of(2022, 8, 20))
                .checkOutDate(LocalDate.of(2022, 8, 22))
                .isCanceled(false)
                .createTime(LocalDateTime.of(2022, 8, 15, 1, 1))
                .updateTime(LocalDateTime.of(2022, 8, 15, 1, 1))
                .reservedBy("customer2")
                .build();

        Booking r3 = Booking.builder()
                .checkInDate(LocalDate.of(2022, 9, 22))
                .checkOutDate(LocalDate.of(2022, 9, 25))
                .isCanceled(false)
                .createTime(LocalDateTime.of(2022, 8, 15, 1, 1))
                .updateTime(LocalDateTime.of(2022, 8, 15, 1, 1))
                .reservedBy("customer3")
                .build();

        Booking r4 = Booking.builder()
                .checkInDate(LocalDate.of(2022, 9, 26))
                .checkOutDate(LocalDate.of(2022, 9, 27))
                .isCanceled(false)
                .createTime(LocalDateTime.of(2022, 8, 15, 1, 1))
                .updateTime(LocalDateTime.of(2022, 8, 15, 1, 1))
                .reservedBy("customer4")
                .build();

        Booking r5 = Booking.builder()
                .checkInDate(LocalDate.of(2022, 9, 26))
                .checkOutDate(LocalDate.of(2022, 9, 27))
                .isCanceled(true)
                .createTime(LocalDateTime.of(2022, 8, 15, 1, 1))
                .updateTime(LocalDateTime.of(2022, 8, 15, 1, 1))
                .reservedBy("customer5")
                .build();

        List<Booking> bookings = Arrays.asList(r1, r2, r3, r4,r5);
        bookings.forEach(reserve -> bookingRepository.save(reserve));
    }
}
