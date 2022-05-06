package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findScreeningsByMovie(Movie movie);

    List<Screening> findScreeningsByRoom(Room room);

    List<Screening> findScreeningsByStartDateIsAfter(LocalDateTime dateTime, Sort sort);

    @Query(value =
                    "SELECT r.name as roomName, s.number as seatNumber, s.row as seatRow " +
                    "FROM Screening AS sc " +
                    "INNER JOIN Movie AS m ON m.id = sc.movie_id " +
                    "INNER JOIN Room AS r ON r.id = sc.room_id " +
                    "INNER JOIN Room_Seats AS rs ON r.id = rs.room_id " +
                    "INNER JOIN Seat AS s ON rs.seats_id = s.id " +
                    "LEFT JOIN Booking_Seats AS bs ON s.id = bs.seats_id " +
                    "WHERE bs.seats_id IS NULL AND sc.id = :screeningId",
            nativeQuery = true)
    List<Tuple> findAvailableSeats(@Param("screeningId") Long screeningId);

    List<Screening> deleteByIdIn(Collection<Long> ids);
}
