package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.assembler.RoomAssembler;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.exception.EntityNotFoundException;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import com.babior.ticketbookingapp.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
public class RoomController {

    private RoomService service;

    @GetMapping("/rooms")
    public CollectionModel<EntityModel<Room>> getAllRooms() {
        List<EntityModel<Room>> rooms = service.findAllRooms();
        return CollectionModel.of(rooms, linkTo(methodOn(RoomController.class).getAllRooms()).withSelfRel());
    }

    @GetMapping("/rooms/{id}")
    public EntityModel<Room> getRoomById(@PathVariable Long id) {
        return service.findRoomById(id);
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        service.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
