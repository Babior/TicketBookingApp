package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.controller.MovieController;
import com.babior.ticketbookingapp.controller.ScreeningController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MovieAssembler implements RepresentationModelAssembler<Movie, EntityModel<Movie>> {
    @Override
    public EntityModel<Movie> toModel(Movie movie) {
        return EntityModel.of(movie,
                linkTo(methodOn(MovieController.class).getMovieById(movie.getId())).withSelfRel(),
                linkTo(methodOn(MovieController.class).getAllMovies()).withRel("movies"));
    }

    @Override
    public CollectionModel<EntityModel<Movie>> toCollectionModel(Iterable<? extends Movie> entities) {
        CollectionModel<EntityModel<Movie>> moviesCollection = RepresentationModelAssembler.super.toCollectionModel(entities);
        moviesCollection.add(linkTo(methodOn(MovieController.class).getAllMovies()).withSelfRel());
        return moviesCollection;
    }
}
