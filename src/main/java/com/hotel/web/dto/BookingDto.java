package com.hotel.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingDto {
    private Long bookingId;
    private String reservedBy;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
