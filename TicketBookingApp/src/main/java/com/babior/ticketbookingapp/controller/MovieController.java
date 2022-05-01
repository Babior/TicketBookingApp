package com.babior.ticketbookingapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.babior.ticketbookingapp.assembler.MovieAssembler;
import com.babior.ticketbookingapp.business.Movie;
import com.babior.ticketbookingapp.business.Screening;
import com.babior.ticketbookingapp.exception.notfound.MovieNotFoundException;
import com.babior.ticketbookingapp.repository.MovieRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
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

    private MovieRepository repository;
    private ScreeningRepository screeningRepository;
    private MovieAssembler assembler;
    private ScreeningController screeningController;


    @GetMapping("/movies")
    public CollectionModel<EntityModel<Movie>> findAllMovies() {
        List<EntityModel<Movie>> movies = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(movies, linkTo(methodOn(MovieController.class).findAllMovies()).withSelfRel());
    }

    @GetMapping("/movies/{id}")
    public EntityModel<Movie> findMovieById(@PathVariable Long id) {
        Movie movie = repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        return assembler.toModel(movie);
    }

    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestBody Movie newMovie) {
        EntityModel<Movie> entityModel = assembler.toModel(repository.save(newMovie));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<?> saveOrUpdateMovie(@RequestBody Movie newMovie, @PathVariable Long id) {
        Movie updatedMovie = repository.findById(id)
                .map(movie -> {
                    movie.setTitle(newMovie.getTitle());
                    movie.setRunningTime(newMovie.getRunningTime());
                    return repository.save(movie);
                }).orElseGet(() -> {
                    newMovie.setId(id);
                    return repository.save(newMovie);
                });
        EntityModel<Movie> entityModel = assembler.toModel(updatedMovie);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        return repository.findById(id).map(movie -> {
            List<Screening> screenings = screeningRepository.findScreeningsByMovie(movie);
            for(Screening s : screenings){
                screeningController.deleteScreening(s.getId());
            }
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new MovieNotFoundException(id));
    }
}
