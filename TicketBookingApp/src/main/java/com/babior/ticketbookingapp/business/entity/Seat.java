package com.babior.ticketbookingapp.business.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"number", "row"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Seat {
    @Id
    @GeneratedValue(generator = "seat_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seat_sequence", sequenceName = "seat_sequence", allocationSize = 1)
    private Long id;
    private Long number;
    private Long row;
}
