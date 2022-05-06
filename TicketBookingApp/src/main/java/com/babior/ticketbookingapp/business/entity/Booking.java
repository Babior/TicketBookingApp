package com.babior.ticketbookingapp.business.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"firstName", "lastName", "expiryDate"})
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
//    @ManyToMany
//    @JoinTable(
//            name = "booking_seats",
//            joinColumns = @JoinColumn(name = "booking_id"),
//            inverseJoinColumns = @JoinColumn(name = "seat_id"))
//    @ToString.Exclude
//    private List<Seat> seats;
    @OneToMany(mappedBy = "seat")
    private List<BookingSeat> seats;
    private String firstName;
    private String lastName;
    private LocalDateTime expiryDate;
    private Double totalPrice = 0.0;
}
