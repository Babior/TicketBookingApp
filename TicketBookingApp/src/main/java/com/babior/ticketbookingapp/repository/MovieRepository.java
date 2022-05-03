package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

}
