package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.business.dto.BookingDTO;
import com.babior.ticketbookingapp.business.entity.*;
import com.babior.ticketbookingapp.exception.EntityNotFoundException;
import com.babior.ticketbookingapp.exception.notallowed.NameNotAllowedException;
import com.babior.ticketbookingapp.exception.notallowed.NoSeatsNotAllowed;
import com.babior.ticketbookingapp.exception.notallowed.TimeNotAllowedException;
import com.babior.ticketbookingapp.repository.BookingRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import com.babior.ticketbookingapp.repository.SeatRepository;
import com.babior.ticketbookingapp.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
public class BookingController {

    private final BookingRepository repository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;

    private final BookingService service;

    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings() {
        List<EntityModel<Booking>> bookings = service.findAllBookings();
        return ResponseEntity.ok(
                CollectionModel.of(bookings, linkTo(methodOn(BookingController.class).getAllBookings()).withSelfRel()));
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findBookingById(id));
    }

    @PostMapping("/bookings")
    public ResponseEntity<EntityModel<Booking>> addBooking(@RequestBody BookingDTO request) {
        return ResponseEntity.ok(service.createBooking(request));
    }

    @PostMapping(path = "/screenings/{screeningId}")
    public ResponseEntity<EntityModel<Booking>> addBooking(@PathVariable Long screeningId, @RequestBody BookingDTO request) {
        request.setScreeningId(screeningId);
        return addBooking(request);
    }

}
