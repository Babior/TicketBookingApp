package com.babior.ticketbookingapp.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicketType {

    ADULT(25.0),
    STUDENT(18.0),
    CHILD(12.5);

    private Double price;
}
