package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.entity.Booking;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.business.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByScreening(Screening screening);

    @Query(value = "select b " +
            "from booking as b " +
            "inner join booking_seat bs on b.id = bs.booking_id " +
            "where bs.seat_id = :seatId ;", nativeQuery = true)
    Set<Booking> findBookingsBySeatId(@NotNull Long seatId);

    List<Booking> deleteByIdIn(Collection<Long> ids);
}
