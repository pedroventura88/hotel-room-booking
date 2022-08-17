package com.hotel.web.exceptions;

import com.hotel.Errors;
import com.hotel.core.Error;

public class ExceptionFormatter extends Errors {
    private String message;
    private String aditionalMessage;

    public ExceptionFormatter(String message, String aditionalMessage) {
        this.message = message;
        this.aditionalMessage = aditionalMessage;
    }

    private Error buildError() {
        return Error.builder().message(message).aditionalMessage(aditionalMessage).build();
    }

    public Errors buildErrors() {
        return addErrorsItem(buildError());
    }

}
