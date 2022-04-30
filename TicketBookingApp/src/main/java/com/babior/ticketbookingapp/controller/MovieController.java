package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.business.Movie;
import com.babior.ticketbookingapp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@RestController
public class MovieController {

    @Autowired
    private MovieService service;

    @PostMapping("/addMovie")
    public Movie addMovie(@RequestBody Movie movie) {
        return service.saveMovie(movie);
    }

    @PostMapping("/addMovies")
    public List<Movie> addMovies(@RequestBody List<Movie> movies) {
        return service.saveMovies(movies);
    }

    @GetMapping("/movies")
    public List<Movie> findAllMovies() {
        return service.getMovies();
    }

    @GetMapping("/movie/{id}")
    public Movie findMoviesById(@PathVariable int id) {
        return service.getMovieById(id);
    }
}
