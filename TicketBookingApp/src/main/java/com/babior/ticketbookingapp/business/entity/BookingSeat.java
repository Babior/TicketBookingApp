package com.babior.ticketbookingapp.business.entity;


import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class BookingSeat {
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    Booking booking;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    Seat seat;

    TicketType ticketType;

}
