package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.dto.ScreeningRequest;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.controller.MovieController;
import com.babior.ticketbookingapp.controller.ScreeningController;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@AllArgsConstructor
public class ScreeningAssembler implements RepresentationModelAssembler<Screening,  EntityModel<ScreeningRepresentation>> {
    private final ModelMapper modelMapper;

    @SneakyThrows
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

    @SneakyThrows
    @Override
    public CollectionModel<EntityModel<ScreeningRepresentation>> toCollectionModel(Iterable<? extends Screening> entities) {
        CollectionModel<EntityModel<ScreeningRepresentation>> screeningRepresentations = RepresentationModelAssembler.super.toCollectionModel(entities);
        screeningRepresentations.add(linkTo(methodOn(ScreeningController.class).getAllScreenings()).withSelfRel());
        return screeningRepresentations;
    }

    @NotNull
    public Screening toEntity(@NotNull ScreeningRequest request) {
        return modelMapper.map(request, Screening.class);
    }

    @NotNull
    public Collection<Screening> toEntities(@NotNull Collection<ScreeningRequest> question) {
        return question.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
