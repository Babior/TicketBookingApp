package com.babior.ticketbookingapp.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Booking {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Screening screening;
    @ManyToMany
    private List<Seat> seats;
    private String firstName;
    private String lastName;
    private TicketType ticketType;
    private LocalDateTime expiryDate;
    private Double totalPrice = 0.0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return firstName.equals(booking.firstName) && lastName.equals(booking.lastName) && expiryDate.equals(booking.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, expiryDate);
    }
}
