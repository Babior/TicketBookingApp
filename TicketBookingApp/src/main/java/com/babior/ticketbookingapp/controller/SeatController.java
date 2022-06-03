package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.dto.SeatDTO;
import com.babior.ticketbookingapp.business.entity.Seat;
import com.babior.ticketbookingapp.service.SeatService;
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
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/seats")
    public ResponseEntity<?> getAllSeats() {
        CollectionModel<EntityModel<SeatDTO>> seats = seatService.findAllSeats();
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/seats/{id}")
    public ResponseEntity<?> getSeatById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(seatService.findSeatById(id));
    }

    @PostMapping("/seats")
    public ResponseEntity<?> addSeat(@RequestBody SeatDTO newSeat) {
        EntityModel<SeatDTO> entityModel = seatService.createSeat(newSeat);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/seats/{id}")
    public ResponseEntity<?> updateSeat(@RequestBody SeatDTO newSeat, @PathVariable @NotNull Long id) {
        EntityModel<SeatDTO> updatedSeat = seatService.saveOrUpdateSeat(newSeat, id);
        return ResponseEntity
                .created(updatedSeat.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(updatedSeat);
    }

    @DeleteMapping("/seats/{id}")
    public ResponseEntity<?> deleteSeat(@PathVariable @NotNull Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }
}
