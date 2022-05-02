package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.business.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findScreeningsByMovie(Movie movie);

    List<Screening> findScreeningsByRoom(Room room);

    List<Screening> findScreeningsByStartDateIsAfter(LocalDateTime dateTime);

    @Query(value =
                    "SELECT m.title, r.name, s.number, s.row " +
                    "FROM Screening AS sc " +
                    "INNER JOIN Movie AS m ON m.id = sc.movie_id " +
                    "INNER JOIN Room AS r ON r.id = sc.room_id " +
                    "INNER JOIN Room_Seats AS rs ON r.id = rs.room_id " +
                    "INNER JOIN Seat AS s ON rs.seats_id = s.id " +
                    "LEFT JOIN Booking_Seat AS bs ON s.id = bs.seat_id " +
                    "WHERE bs.seat_id = null",
            nativeQuery = true)
    List<Object> findAvailableSeats();
}
