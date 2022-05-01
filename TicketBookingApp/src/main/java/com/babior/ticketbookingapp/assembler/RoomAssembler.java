package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.Room;
import com.babior.ticketbookingapp.controller.RoomController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RoomAssembler implements RepresentationModelAssembler<Room, EntityModel<Room>> {
    @Override
    public EntityModel<Room> toModel(Room room) {
        return EntityModel.of(room,
                linkTo(methodOn(RoomController.class).findRoomById(room.getId())).withSelfRel(),
                linkTo(methodOn(RoomController.class).findAllRooms()).withRel("room"));
    }
}
