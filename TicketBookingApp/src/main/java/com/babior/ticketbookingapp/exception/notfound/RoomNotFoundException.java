package com.babior.ticketbookingapp.exception.notfound;

public class RoomNotFoundException extends NotFoundException {
    public RoomNotFoundException(Long id) {
        super("room", id);
    }
}
