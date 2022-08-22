package com.hotel.web.exceptions;

import com.hotel.core.error.Errors;

public class BookingNotFoundException extends RuntimeException {
    private static final long serialVersionID = 1L;

    private Errors errors;

    public BookingNotFoundException(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
