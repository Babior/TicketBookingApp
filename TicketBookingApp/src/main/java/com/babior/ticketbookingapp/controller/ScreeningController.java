package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.business.dto.RoomRepresentation;
import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.entity.*;
import com.babior.ticketbookingapp.business.dto.ScreeningRequest;
import com.babior.ticketbookingapp.service.ScreeningService;
import com.babior.ticketbookingapp.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
public class ScreeningController {

    private final ScreeningService service;
    private final SeatService seatService;


    @GetMapping("/screenings")
    public ResponseEntity<?> getAllScreenings() {
        CollectionModel<EntityModel<ScreeningRepresentation>> screenings = service.findAllScreenings();
        return ResponseEntity.ok(
                CollectionModel.of(screenings, linkTo(methodOn(MovieController.class).getAllMovies()).withSelfRel()));
    }

    @GetMapping(value = "/screenings/{id}")
    public ResponseEntity<?> getScreeningById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findScreeningById(id));
    }

    @GetMapping("/screenings/after/{dateTimeString}")
    public ResponseEntity<?> getAllScreeningsStartingFrom(@PathVariable String dateTimeString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        if (dateTime.isBefore(LocalDateTime.now().plusMinutes(15))) {
            dateTime = LocalDateTime.now().plusMinutes(15);
        }
        return ResponseEntity.ok(service.findScreeningsAfterDate(dateTime));
    }

    @GetMapping(path = "/screenings/{screeningId}/seats")
    public ResponseEntity<?> getSeatsInScreening(@PathVariable Long screeningId) {
        // TODO: Как сделать через сервис, если он возвращает EntityModeL<Screening>?
        // + кривые ссылки при возвращении
        var roomRepresentation = RoomRepresentation.builder()
                .roomName(service.findRoomNameByScreening(screeningId))
                .availableSeats(seatService.findAvailableSeatsByScreening(screeningId))
                .build();
        return ResponseEntity.ok(roomRepresentation);
//                CollectionModel.of(roomRepresentation,
//                        linkTo(methodOn(RoomController.class).getAllRooms()).withSelfRel(),
//                        linkTo(methodOn(SeatController.class).getAllSeats()).withSelfRel()));
    }

    @PostMapping("/screenings")
    public ResponseEntity<?> addScreening(@RequestBody ScreeningRequest request) {
        EntityModel<ScreeningRepresentation> screeningRepresentation = service.createScreening(request);
        return ResponseEntity
                .created(screeningRepresentation.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(screeningRepresentation);
    }

    @DeleteMapping("/screenings/{id}")
    public ResponseEntity<?> deleteScreening(@PathVariable Long id) {
        return null;
    }
}
