package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

}
