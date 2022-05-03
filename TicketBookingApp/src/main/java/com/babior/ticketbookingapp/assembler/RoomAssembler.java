package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.entity.Room;
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
                linkTo(methodOn(RoomController.class).getRoomById(room.getId())).withSelfRel(),
                linkTo(methodOn(RoomController.class).getAllRooms()).withRel("room"));
        //link to the seats that are assigned to this room
    }
}
