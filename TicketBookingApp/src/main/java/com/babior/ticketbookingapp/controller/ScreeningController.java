package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.business.dto.RoomRepresentation;
import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.entity.*;
import com.babior.ticketbookingapp.business.dto.ScreeningDTO;
import com.babior.ticketbookingapp.service.ScreeningService;
import com.babior.ticketbookingapp.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
public class ScreeningController {

    private final ScreeningService screeningService;
    private final SeatService seatService;

    @GetMapping("/screenings")
    public ResponseEntity<?> getAllScreenings() {
        CollectionModel<EntityModel<ScreeningRepresentation>> screenings = screeningService.findAllScreenings();
        return ResponseEntity.ok(screenings);
    }

    @GetMapping(value = "/screenings/{id}")
    public ResponseEntity<?> getScreeningById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(screeningService.findScreeningById(id));
    }

    @GetMapping(path = "/screenings/after/{dateTimeString}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllScreeningsStartingFrom(@PathVariable String dateTimeString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        if (dateTime.isBefore(LocalDateTime.now().plusMinutes(15))) {
            dateTime = LocalDateTime.now().plusMinutes(15);
        }
        return ResponseEntity.ok(screeningService.findScreeningsAfterDate(dateTime));
    }

    @GetMapping(path = "/screenings/{screeningId}/seats")
    public ResponseEntity<?> getSeatsInScreening(@PathVariable @NotNull Long screeningId) {
        var roomRepresentation = RoomRepresentation.builder()
                .roomName(screeningService.findRoomNameByScreening(screeningId))
                .availableSeats(seatService.findAvailableSeatsByScreening(screeningId))
                .build();
        return ResponseEntity.ok(roomRepresentation);
    }

    @PostMapping("/screenings")
    public ResponseEntity<?> addScreening(@RequestBody ScreeningDTO request) {
        EntityModel<ScreeningRepresentation> screeningRepresentation = screeningService.createScreening(request);
        return ResponseEntity
                .created(screeningRepresentation.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(screeningRepresentation);
    }

    @PutMapping("/screenings/{id}")
    public ResponseEntity<?> updateScreening(@RequestBody ScreeningDTO newScreening, @PathVariable @NotNull Long id) {
        EntityModel<ScreeningRepresentation> updatedScreening= screeningService.saveOrUpdateScreening(newScreening, id);
        return ResponseEntity
                .created(updatedScreening.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(updatedScreening);
    }

    @DeleteMapping("/screenings/{id}")
    public ResponseEntity<?> deleteScreening(@PathVariable @NotNull Long id) {
        screeningService.deleteScreening(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }
}
