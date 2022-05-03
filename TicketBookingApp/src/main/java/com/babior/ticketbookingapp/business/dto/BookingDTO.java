package com.babior.ticketbookingapp.business.dto;

import com.babior.ticketbookingapp.business.entity.*;
import com.sun.istack.NotNull;
import javax.validation.constraints.Min;
import lombok.Data;

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
    @NotNull
    private List<Integer> seats;

}
