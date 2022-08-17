package com.hotel.core.db;

import com.hotel.core.reservation.Reservation;
import com.hotel.core.reservation.ReservationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DBInitializer {

    private ReservationRepository reservationRepository;

    public DBInitializer(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        populate();
    }

    private void populate() {
        Reservation r1 = Reservation.builder()
                .entranceDate(LocalDate.of(2022,8,17))
                .checkOut(LocalDate.of(2022,8,27))
                .isCanceled(false)
                .createTime(LocalDateTime.of(2022,8,15,1,1))
                .updateTime(LocalDateTime.of(2022,8,15,1,1))
                .reservedBy("customer1")
                .build();


        Reservation r2 = Reservation.builder()
                .entranceDate(LocalDate.of(2022,8,19))
                .checkOut(LocalDate.of(2022,9,1))
                .isCanceled(false)
                .createTime(LocalDateTime.of(2022,8,15,1,1))
                .updateTime(LocalDateTime.of(2022,8,15,1,1))
                .reservedBy("customer2")
                .build();

        List<Reservation> reservations = Arrays.asList(r1,r2);
        reservations.forEach(reserve -> reservationRepository.save(reserve));
    }
}
