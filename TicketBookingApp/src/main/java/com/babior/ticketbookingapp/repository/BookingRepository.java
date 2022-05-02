package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
