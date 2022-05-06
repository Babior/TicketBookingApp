package com.babior.ticketbookingapp.business.entity;


import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(of ={"booking", "seat"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class BookingSeat {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private TicketType ticketType;

}
