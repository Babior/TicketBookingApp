package com.babior.ticketbookingapp.exception.notfound;

public class SeatNotFoundException extends NotFoundException {
    public SeatNotFoundException(Long id) {
        super("seat", id);
    }
}
