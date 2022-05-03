package com.babior.ticketbookingapp.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Room {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany
    private List<Seat> seats;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return name.equals(room.name) && seats.equals(room.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
