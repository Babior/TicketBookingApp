package com.babior.ticketbookingapp.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Screening screening = (Screening) o;
        return movie.equals(screening.movie) && room.equals(screening.room) && startDate.equals(screening.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, room, startDate);
    }
}
