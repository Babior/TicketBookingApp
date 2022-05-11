package com.babior.ticketbookingapp.business.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "booking", collectionRelation = "bookings")
public class BookingRepresentation {
    private String firstName;
    private String lastName;
    private double totalPrice;
    private LocalDateTime expirationDate;
}
