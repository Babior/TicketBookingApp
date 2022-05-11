package com.babior.ticketbookingapp.controller;


import com.babior.ticketbookingapp.business.dto.MovieDTO;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@AllArgsConstructor
public class MovieController {

    private MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<?> getAllMovies() {
        CollectionModel<EntityModel<MovieDTO>> movies = movieService.findAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(movieService.findMovieById(id));
    }

    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestBody MovieDTO newMovie) {
        EntityModel<MovieDTO> entityModel = movieService.createMovie(newMovie);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<?> updateMovie(@RequestBody MovieDTO newMovie, @PathVariable @NotNull Long id) {
        EntityModel<MovieDTO> updatedMovie = movieService.saveOrUpdateMovie(newMovie, id);
        return ResponseEntity
                .created(updatedMovie.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(updatedMovie);
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable @NotNull Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }
}
