package com.babior.ticketbookingapp.exception.notfound;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException(Long id) {
        super("booking", id);
    }
}
