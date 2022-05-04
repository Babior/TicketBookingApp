package com.babior.ticketbookingapp.business.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Room room;
    private LocalDateTime startDate;
}
