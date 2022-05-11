package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.dto.RoomRepresentation;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.controller.RoomController;
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
public class RoomAssembler implements RepresentationModelAssembler<Room, EntityModel<RoomRepresentation>> {
    private final SeatAssembler seatAssembler;
    @Override
    public EntityModel<RoomRepresentation> toModel(Room entity) {
        RoomRepresentation roomRepresentation = RoomRepresentation.builder()
                .roomName(entity.getName())
                .availableSeats(seatAssembler.toCollectionModel(entity.getSeats()))
                .build();
        return EntityModel.of(roomRepresentation,
                linkTo(methodOn(RoomController.class).getRoomById(entity.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<RoomRepresentation>> toCollectionModel(Iterable<? extends Room> entities) {
        CollectionModel<EntityModel<RoomRepresentation>> roomRepresentations = RepresentationModelAssembler.super.toCollectionModel(entities);
        roomRepresentations.add(linkTo(methodOn(RoomController.class).getAllRooms()).withSelfRel());
        return roomRepresentations;
    }
}
