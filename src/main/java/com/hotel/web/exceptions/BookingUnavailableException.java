package com.hotel.web.exceptions;

import com.hotel.Errors;

public class BookingUnavailableException extends RuntimeException {
    private static final long serialVersionID = 1L;

    private Errors errors;

    public BookingUnavailableException(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
