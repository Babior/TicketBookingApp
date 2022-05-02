package com.babior.ticketbookingapp.exception.notallowed;

public class NameNotAllowedException extends NotAllowedException {

    public NameNotAllowedException() {
        super("check the name and surname spelling");
    }
}
