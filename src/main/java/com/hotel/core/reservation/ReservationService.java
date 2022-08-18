package com.hotel.core.reservation;

import com.hotel.core.BookingRequest;

import java.time.LocalDate;

public interface ReservationService {
    String checkRoomAvailability(LocalDate date) throws Exception;
    Reservation makeReservation(BookingRequest bookingRequest) throws Exception;
    void cancelReservation(Long reservationId);
    Reservation updateReservation(Long reservationId, BookingRequest bookingRequest);
}
