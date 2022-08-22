package com.hotel.core.reservation;

import com.hotel.core.BookingRequest;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    String checkRoomAvailability(LocalDate date) throws Exception;

    Booking bookRoom(BookingRequest bookingRequest) throws Exception;

    void cancelBooking(Long reservationId);

    Booking updateBooking(Long reservationId, BookingRequest bookingRequest) throws Exception;

    List<Booking> getAllBookings();
}
