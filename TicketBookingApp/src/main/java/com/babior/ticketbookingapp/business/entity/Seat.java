package com.babior.ticketbookingapp.business.entity;

import com.babior.ticketbookingapp.business.dto.SeatDTO;
import com.babior.ticketbookingapp.controller.SeatController;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@NamedNativeQuery(name = "Seat.findAvailableSeatsByScreening",
                  query = "SELECT s.id as seatId, s.number as seatNumber, s.row as seatRow, r.id as roomId " +
                          "FROM Screening AS sc " +
                          "INNER JOIN Movie AS m ON m.id = sc.movie_id " +
                          "INNER JOIN Room AS r ON r.id = sc.room_id " +
                          "INNER JOIN Room_Seats AS rs ON r.id = rs.room_id " +
                          "INNER JOIN Seat AS s ON rs.seats_id = s.id " +
                          "LEFT JOIN Booking_Seats AS bs ON s.id = bs.seats_id " +
                          "WHERE bs.seats_id IS NULL AND sc.id = :screeningId",
                  resultSetMapping = "Mapping.Seat")
@SqlResultSetMapping(name = "Mapping.Seat",
        classes = @ConstructorResult(targetClass = SeatDTO.class,
                columns = {@ColumnResult(name = "seatNumber", type = Long.class),
                        @ColumnResult(name = "seatRow", type = Long.class),
                        @ColumnResult(name = "roomId", type = Long.class)}))
public class Seat {
    @Id
    @GeneratedValue
    private Long id;
    private Long number;
    private Long row;
    @ManyToOne
    private Room room;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Seat seat = (Seat) o;
        return id != null && Objects.equals(id, seat.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
