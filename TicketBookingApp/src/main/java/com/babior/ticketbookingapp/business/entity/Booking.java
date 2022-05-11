package com.babior.ticketbookingapp.business.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    @GeneratedValue(generator = "booking_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "booking_sequence", sequenceName = "booking_sequence", allocationSize = 1)
    private Long id;
    @ManyToOne
    private Screening screening;
    @ElementCollection
    @Column(name = "ticket_type", nullable = false)
    @JoinTable(
            name = "booking_seat",
            joinColumns = @JoinColumn(name = "booking_id")
    )
    @MapKeyJoinColumn(name = "seat_id")
    @Enumerated(EnumType.STRING)
    private Map<Seat, TicketType> seats = new HashMap<>();
    private String firstName;
    private String lastName;
    private LocalDateTime expiryDate;
    private Double totalPrice = 0.0;
}
