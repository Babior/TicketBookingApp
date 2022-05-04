package com.babior.ticketbookingapp.business.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

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
    @GeneratedValue
    private Long id;
    private Long number;
    private Long row;

}
