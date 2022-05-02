package com.babior.ticketbookingapp.business.dto;

import com.babior.ticketbookingapp.business.entity.*;
import com.sun.istack.NotNull;
import javax.validation.constraints.Min;
import lombok.Data;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDTO {
    private Long screeningId;
    @NotNull
    @Min(3)
    private String firstName;
    @NotNull
    @Min(3)
    private String lastName;
    private List<Integer> seats;


    public Booking convertToEntity(Screening screening) {
        Booking entity = new Booking();
        entity.setScreening(screening);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setExpiryDate(screening.getStartDate().minusMinutes(15));
        return entity;
    }
}
