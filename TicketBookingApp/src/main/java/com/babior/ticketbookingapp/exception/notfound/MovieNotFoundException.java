package com.babior.ticketbookingapp.exception.notfound;

public class MovieNotFoundException extends NotFoundException {
    public MovieNotFoundException(Long id) {
        super("movie", id);
    }
}
