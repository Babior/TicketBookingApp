package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

}
