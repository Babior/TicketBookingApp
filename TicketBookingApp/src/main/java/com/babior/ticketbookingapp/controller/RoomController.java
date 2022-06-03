package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.business.dto.RoomDTO;
import com.babior.ticketbookingapp.business.dto.RoomRepresentation;
import com.babior.ticketbookingapp.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.NotNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
public class RoomController {

    private RoomService roomService;

    @GetMapping("/rooms")
    public ResponseEntity<?> getAllRooms() {
        CollectionModel<EntityModel<RoomRepresentation>> rooms = roomService.findAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(roomService.findRoomById(id));
    }

    @PostMapping("/rooms")
    public ResponseEntity<?> addRoom(@RequestBody RoomDTO newRoom) {
        EntityModel<RoomRepresentation> entityModel = roomService.createRoom(newRoom);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<?> saveOrUpdateRoom(@RequestBody RoomDTO newRoom, @PathVariable @NotNull Long id) {
        EntityModel<RoomRepresentation> updatedRoom = roomService.saveOrUpdateRoom(newRoom, id);
        return ResponseEntity
                .created(updatedRoom.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(updatedRoom);
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable @NotNull Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }
}
