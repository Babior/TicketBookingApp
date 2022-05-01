package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.assembler.RoomAssembler;
import com.babior.ticketbookingapp.business.Movie;
import com.babior.ticketbookingapp.business.Room;
import com.babior.ticketbookingapp.business.Screening;
import com.babior.ticketbookingapp.exception.notfound.MovieNotFoundException;
import com.babior.ticketbookingapp.exception.notfound.RoomNotFoundException;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        return repository.findById(id).map(room -> {
            List<Screening> screenings = screeningRepository.findScreeningsByRoom(room);
            for(Screening s : screenings){
                screeningController.deleteScreening(s.getId());
            }
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new RoomNotFoundException(id));
    }
}