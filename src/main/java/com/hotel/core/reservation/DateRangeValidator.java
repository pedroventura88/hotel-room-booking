package com.hotel.core.reservation;

import java.time.LocalDate;

public class DateRangeValidator {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRangeValidator(LocalDate checkInDate, LocalDate checkOutDate) {
        this.startDate = checkInDate;
        this.endDate = checkOutDate;
    }

    public boolean isWithinRange(LocalDate testDate) {
        if ((testDate.equals(startDate) || testDate.equals(endDate)) ||
                (testDate.isAfter(startDate) && testDate.isBefore(endDate))) {
            return true;
        }
        return false;
    }
}
