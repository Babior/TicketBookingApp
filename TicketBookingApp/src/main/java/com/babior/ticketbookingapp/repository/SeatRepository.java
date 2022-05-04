package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query(value = "select s.id, s.number, s.row " +
            "from seat as s " +
            "inner join room r on r.id = s.room_id " +
            "inner join screening sc on r.id = sc.room_id " +
            "left join booking b on sc.id = b.screening_id " +
            "left join booking_seat bs on b.id = bs.booking_id and s.id = bs.seat_id " +
            "where sc.id = :screeningId and bs.seat_id IS NULL;", nativeQuery = true)
    List<Seat> findAvailableSeatsByScreening(@NotNull Long screeningId);

    List<Seat> findAllById(@NotNull List<Long> ids);
}
