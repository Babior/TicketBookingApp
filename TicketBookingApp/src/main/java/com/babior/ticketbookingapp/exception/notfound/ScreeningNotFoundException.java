package com.babior.ticketbookingapp.exception.notfound;

public class ScreeningNotFoundException extends NotFoundException {
    public ScreeningNotFoundException(Long id) {
        super("screening", id);
    }
}
