package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
