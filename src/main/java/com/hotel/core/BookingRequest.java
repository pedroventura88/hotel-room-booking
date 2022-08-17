package com.hotel.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

    @Positive
    private String customerName;
}
