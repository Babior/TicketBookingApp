package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.business.dto.BookingRepresentation;
import com.babior.ticketbookingapp.business.dto.BookingDTO;
import com.babior.ticketbookingapp.business.entity.Booking;
import com.babior.ticketbookingapp.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings() {
        CollectionModel<EntityModel<BookingRepresentation>> bookings = bookingService.findAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @PostMapping("/bookings")
    public ResponseEntity<EntityModel<BookingRepresentation>> addBooking(@RequestBody BookingDTO request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @PostMapping(path = "/bookings/screenings/{screeningId}")
    public ResponseEntity<EntityModel<BookingRepresentation>> bookSeats(@PathVariable @NotNull Long screeningId, @RequestBody BookingDTO request) {
        request.setScreeningId(screeningId);
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<?> updateBooking(@RequestBody BookingDTO newBooking, @PathVariable @NotNull Long id) {
        EntityModel<BookingRepresentation> updatedScreening= bookingService.saveOrUpdateBooking(newBooking, id);
        return ResponseEntity
                .created(updatedScreening.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(updatedScreening);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable @NotNull Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }
}
