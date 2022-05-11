package com.babior.ticketbookingapp.exception;

import static java.lang.String.format;

public class NotAllowedException extends RuntimeException {

    public NotAllowedException(String message) {
        super(message);
    }

    public NotAllowedException(String entityName, Long identifier) {
        super(format("%s(%s) is illegal", entityName, identifier));
    }
}
