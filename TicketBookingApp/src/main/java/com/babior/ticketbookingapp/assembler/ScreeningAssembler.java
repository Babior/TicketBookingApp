package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.controller.MovieController;
import com.babior.ticketbookingapp.controller.ScreeningController;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@AllArgsConstructor
public class ScreeningAssembler implements RepresentationModelAssembler<Screening,  EntityModel<ScreeningRepresentation>> {

    @Override
    public EntityModel<ScreeningRepresentation> toModel(Screening entity) {
        ScreeningRepresentation screeningRepresentation = ScreeningRepresentation.builder()
                .movieTitle(entity.getMovie().getTitle())
                .startDate(entity.getStartDate())
                .build();

        return EntityModel.of(screeningRepresentation,
                linkTo(methodOn(ScreeningController.class).getScreeningById(entity.getId())).withSelfRel(),
                linkTo(methodOn(MovieController.class).getMovieById(entity.getMovie().getId())).withRel("movie"));
    }

    @Override
    public CollectionModel<EntityModel<ScreeningRepresentation>> toCollectionModel(Iterable<? extends Screening> entities) {
        CollectionModel<EntityModel<ScreeningRepresentation>> screeningRepresentations = RepresentationModelAssembler.super.toCollectionModel(entities);
        screeningRepresentations.add(linkTo(methodOn(ScreeningController.class).getAllScreenings()).withSelfRel());
        return screeningRepresentations;
    }
}
