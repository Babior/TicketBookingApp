package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.assembler.RoomAssembler;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.exception.notfound.RoomNotFoundException;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
public class RoomController {

    private RoomRepository repository;
    private ScreeningRepository screeningRepository;
    private ScreeningController screeningController;
    private RoomAssembler assembler;

    @GetMapping("/rooms")
    public CollectionModel<EntityModel<Room>> findAllRooms() {
        List<EntityModel<Room>> rooms = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(rooms, linkTo(methodOn(RoomController.class).findAllRooms()).withSelfRel());
    }

    @GetMapping("/rooms/{id}")
    public EntityModel<Room> findRoomById(@PathVariable Long id) {
        Room room = repository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
        return assembler.toModel(room);
    }

//    @PostMapping("/rooms")
//    public ResponseEntity<?> addRoom(@RequestBody Room newRoom) {
//        EntityModel<Room> entityModel = assembler.toModel(repository.save(newRoom));
//        return ResponseEntity
//                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
//                .body(entityModel);
//    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        return repository.findById(id).map(room -> {
            List<Screening> screenings = screeningRepository.findScreeningsByRoom(room);
            for (Screening s : screenings) {
                screeningController.deleteScreening(s.getId());
            }
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new RoomNotFoundException(id));
    }
}
