package com.babior.ticketbookingapp.controller;

import java.util.List;

import com.babior.ticketbookingapp.assembler.MovieAssembler;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.repository.MovieRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import com.babior.ticketbookingapp.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@AllArgsConstructor
public class MovieController {

    private MovieService service;

    @GetMapping("/movies")
    public ResponseEntity<?> getAllMovies() {
        List<EntityModel<Movie>> movies = service.findAllMovies();
        return ResponseEntity.ok(
                CollectionModel.of(movies, linkTo(methodOn(MovieController.class).getAllMovies()).withSelfRel()));
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findMovieById(id));
    }

    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestBody Movie newMovie) {
        EntityModel<Movie> entityModel = service.createMovie(newMovie);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<?> saveOrUpdateMovie(@RequestBody Movie newMovie, @PathVariable Long id) {
        EntityModel<Movie> updatedMovie = service.updateMovie(newMovie, id);
        return ResponseEntity
                .created(updatedMovie.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(updatedMovie);
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        service.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
