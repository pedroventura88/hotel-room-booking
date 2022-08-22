package com.hotel.core.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsBookingByCheckInDate(LocalDate checkIn);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Booking> findByIdAndIsCanceledFalse(Long reservationId);
}
