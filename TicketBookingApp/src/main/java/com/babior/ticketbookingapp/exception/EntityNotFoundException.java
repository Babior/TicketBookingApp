package com.babior.ticketbookingapp.exception;

import static java.lang.String.format;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, Long identifier) {
        super(format("%s(%s) not found", entityName, identifier));
    }
}
