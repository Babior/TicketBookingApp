package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.dto.SeatDTO;
import com.babior.ticketbookingapp.business.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query(nativeQuery = true)
    List<SeatDTO> findAvailableSeatsByScreening(Long screeningId);

}
