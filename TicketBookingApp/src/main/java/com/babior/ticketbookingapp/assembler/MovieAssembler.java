package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.Movie;
import com.babior.ticketbookingapp.controller.MovieController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MovieAssembler implements RepresentationModelAssembler<Movie, EntityModel<Movie>> {
    @Override
    public EntityModel<Movie> toModel(Movie movie) {
        return EntityModel.of(movie,
                linkTo(methodOn(MovieController.class).findMovieById(movie.getId())).withSelfRel(),
                linkTo(methodOn(MovieController.class).findAllMovies()).withRel("movies"));
    }
}
