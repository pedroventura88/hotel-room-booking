package com.hotel.core.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookingRequest {
    @Future
    @NotNull
    private LocalDate checkkIn;

    @NotNull
    private LocalDate checkOut;

    @NotNull(message = "null value not allowed")
    private String customerName;
}
