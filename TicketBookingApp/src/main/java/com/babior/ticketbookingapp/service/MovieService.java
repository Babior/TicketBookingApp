package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.MovieAssembler;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.exception.EntityNotFoundException;
import com.babior.ticketbookingapp.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class MovieService {

    private MovieRepository repository;
    private MovieAssembler assembler;
    private ScreeningService screeningService;

    public List<EntityModel<Movie>> findAllMovies() {
        return repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Movie> findMovieById(@NotNull Long id) {
        Movie movie = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Movie.class.getSimpleName(), id));
        return assembler.toModel(movie);
    }

    public EntityModel<Movie> createMovie(Movie newMovie) {
        return assembler.toModel(repository.save(newMovie));
    }

    public EntityModel<Movie> updateMovie(Movie newMovie, @NotNull Long id) {
        Movie updatedMovie = repository.findById(id)
                .map(movie -> {
                    movie.setTitle(newMovie.getTitle());
                    movie.setRunningTime(newMovie.getRunningTime());
                    return repository.save(movie);
                }).orElseGet(() -> {
                    newMovie.setId(id);
                    return repository.save(newMovie);
                });
        return assembler.toModel(updatedMovie);
    }

    public void deleteMovie(@NotNull Long id) {
        Movie movie = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Movie.class.getSimpleName(), id));
        screeningService.deleteByMovie(movie);
        repository.deleteById(id);
    }

}
