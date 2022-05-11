package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.MovieAssembler;
import com.babior.ticketbookingapp.business.dto.MovieDTO;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.exception.EntityNotFoundException;
import com.babior.ticketbookingapp.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;


@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private MovieRepository movieRepository;
    private MovieAssembler movieAssembler;
    private ScreeningService screeningService;

    @NotNull
    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<MovieDTO>> findAllMovies() {
        return movieAssembler.toCollectionModel(movieRepository.findAll());
    }

    @NotNull
    @Transactional(readOnly = true)
    public EntityModel<MovieDTO> findMovieById(@NotNull Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Movie.class.getSimpleName(), id));
        return movieAssembler.toModel(movie);
    }

    @NotNull
    @Transactional
    public EntityModel<MovieDTO> createMovie(MovieDTO newMovie) {
        Movie movie = Movie
                .builder()
                .title(newMovie.getTitle())
                .runningTime(newMovie.getRunningTime())
                .build();
        return movieAssembler.toModel(movieRepository.save(movie));
    }

    @NotNull
    @Transactional
    public EntityModel<MovieDTO> saveOrUpdateMovie(MovieDTO newMovie, @NotNull Long id) {

        Movie updatedMovie = movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(newMovie.getTitle());
                    movie.setRunningTime(newMovie.getRunningTime());
                    return movieRepository.save(movie);
                }).orElseGet(() -> movieRepository.save(Movie
                        .builder()
                        .id(id)
                        .title(newMovie.getTitle())
                        .runningTime(newMovie.getRunningTime())
                        .build()));
        return movieAssembler.toModel(updatedMovie);
    }

    @Transactional
    public void deleteMovie(@NotNull Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Movie.class.getSimpleName(), id));
        screeningService.deleteByMovie(movie);
        movieRepository.deleteById(id);
    }

}
