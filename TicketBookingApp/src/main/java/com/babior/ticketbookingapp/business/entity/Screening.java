package com.babior.ticketbookingapp.business.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"movie", "room", "startDate"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Screening {
    @Id
    @GeneratedValue(generator = "screening_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "screening_sequence", sequenceName = "screening_sequence", allocationSize = 1)
    private Long id;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Room room;
    private LocalDateTime startDate;
}
