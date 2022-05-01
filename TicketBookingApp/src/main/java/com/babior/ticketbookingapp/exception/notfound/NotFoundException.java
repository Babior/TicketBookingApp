package com.babior.ticketbookingapp.exception.notfound;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String objectName, Long id) {
        super(String.format("Could not find %s %d", objectName, id));
    }
}
