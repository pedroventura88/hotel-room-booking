package com.hotel.web.dto;

import com.hotel.core.reservation.Booking;

public class BookingMapper {
    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .bookingId(booking.getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .reservedBy(booking.getReservedBy())
                .build();
    }
}
