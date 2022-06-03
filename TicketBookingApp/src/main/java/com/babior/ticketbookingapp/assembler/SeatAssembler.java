package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.dto.RoomRepresentation;
import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.dto.SeatDTO;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Seat;
import com.babior.ticketbookingapp.controller.RoomController;
import com.babior.ticketbookingapp.controller.SeatController;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SeatAssembler implements RepresentationModelAssembler<Seat, EntityModel<SeatDTO>> {

    @Override
    public EntityModel<SeatDTO> toModel(Seat entity) {
        SeatDTO seatDTO = SeatDTO.builder()
                .row(entity.getRow())
                .number(entity.getNumber())
                .build();

        return EntityModel.of(seatDTO,
                linkTo(methodOn(SeatController.class).getSeatById(entity.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<SeatDTO>> toCollectionModel(Iterable<? extends Seat> entities) {
        CollectionModel<EntityModel<SeatDTO>> seats = RepresentationModelAssembler.super.toCollectionModel(entities);
        seats.add(linkTo(methodOn(SeatController.class).getAllSeats()).withSelfRel());
        return seats;
    }
}
