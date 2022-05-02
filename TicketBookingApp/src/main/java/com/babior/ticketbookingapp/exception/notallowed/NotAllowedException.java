package com.babior.ticketbookingapp.exception.notallowed;

public class NotAllowedException extends RuntimeException {
    NotAllowedException(String reason) {
        super(String.format("Action is not allowed: %s", reason));
    }
}
