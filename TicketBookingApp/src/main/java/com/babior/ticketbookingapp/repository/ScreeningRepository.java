package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.Movie;
import com.babior.ticketbookingapp.business.Room;
import com.babior.ticketbookingapp.business.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findScreeningsByMovie(Movie movie);

    List<Screening> findScreeningsByRoom(Room room);
}
