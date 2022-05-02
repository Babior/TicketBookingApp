package com.babior.ticketbookingapp.exception.notallowed;

public class NoSeatsNotAllowed extends NotAllowedException {
    public NoSeatsNotAllowed() {
        super("no seats were provided for booking");
    }
}
