package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

}
