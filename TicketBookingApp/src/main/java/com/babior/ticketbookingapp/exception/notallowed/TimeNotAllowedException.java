package com.babior.ticketbookingapp.exception.notallowed;

public class TimeNotAllowedException extends NotAllowedException {
    public TimeNotAllowedException() {
        super("reservations less than 15mins before screening");
    }
}
