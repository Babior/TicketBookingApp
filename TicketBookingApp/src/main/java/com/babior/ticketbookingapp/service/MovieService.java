package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.business.dto.MovieDTO;
import com.babior.ticketbookingapp.business.entity.Movie;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import javax.validation.constraints.NotNull;

public interface MovieService {
    @NotNull
    CollectionModel<EntityModel<MovieDTO>> findAllMovies();

    @NotNull
    EntityModel<MovieDTO> findMovieById(@NotNull Long id);

    @NotNull
    EntityModel<MovieDTO> createMovie(MovieDTO newMovie);

    @NotNull
    EntityModel<MovieDTO> saveOrUpdateMovie(MovieDTO newMovie, @NotNull Long id);

    void deleteMovie(@NotNull Long id);
}
