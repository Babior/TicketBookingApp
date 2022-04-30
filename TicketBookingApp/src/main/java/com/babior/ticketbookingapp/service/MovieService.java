package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.business.Movie;
import com.babior.ticketbookingapp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository repository;

    public Movie saveMovie(Movie movie) {
        return repository.save(movie);
    }

    public List<Movie> saveMovies(List<Movie> movies) {
        return repository.saveAll(movies);
    }

    public List<Movie> getMovies() {
        return repository.findAll();
    }

    public Movie getMovieById(int id) {
        return repository.findById(id).orElse(null);
    }
}
