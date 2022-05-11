package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.dto.MovieDTO;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.controller.MovieController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MovieAssembler implements RepresentationModelAssembler<Movie, EntityModel<MovieDTO>> {
    @Override
    public EntityModel<MovieDTO> toModel(Movie movie) {
        MovieDTO movieDTO = MovieDTO.builder()
                .title(movie.getTitle())
                .runningTime(movie.getRunningTime())
                .build();

        return EntityModel.of(movieDTO,
                linkTo(methodOn(MovieController.class).getMovieById(movie.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<MovieDTO>> toCollectionModel(Iterable<? extends Movie> entities) {
        CollectionModel<EntityModel<MovieDTO>> moviesCollection = RepresentationModelAssembler.super.toCollectionModel(entities);
        moviesCollection.add(linkTo(methodOn(MovieController.class).getAllMovies()).withSelfRel());
        return moviesCollection;
    }
}
