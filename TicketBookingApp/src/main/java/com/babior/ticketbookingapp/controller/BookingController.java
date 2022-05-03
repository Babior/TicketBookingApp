package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.assembler.BookingAssembler;
import com.babior.ticketbookingapp.business.dto.BookingDTO;
import com.babior.ticketbookingapp.business.dto.SeatDTO;
import com.babior.ticketbookingapp.business.entity.Booking;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.business.entity.Seat;
import com.babior.ticketbookingapp.exception.notallowed.NameNotAllowedException;
import com.babior.ticketbookingapp.exception.notallowed.NoSeatsNotAllowed;
import com.babior.ticketbookingapp.exception.notallowed.TimeNotAllowedException;
import com.babior.ticketbookingapp.exception.notfound.BookingNotFoundException;
import com.babior.ticketbookingapp.exception.notfound.MovieNotFoundException;
import com.babior.ticketbookingapp.exception.notfound.ScreeningNotFoundException;
import com.babior.ticketbookingapp.repository.BookingRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import com.babior.ticketbookingapp.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
public class BookingController {

    private final BookingRepository repository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final BookingAssembler assembler;

    @PostMapping("/bookings")
    public ResponseEntity<EntityModel<Booking>> addBooking(@RequestBody BookingDTO request) {
        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(request.getScreeningId()));

        //at latest 15 minutes before the screening begins
        if (screening.getStartDate().isBefore(LocalDateTime.now().plusMinutes(15))) {
            throw new TimeNotAllowedException();
        }
        //check if reservation reserves at least a single seat
        if (request.getSeats() == null || request.getSeats().size() == 0) {
            throw new NoSeatsNotAllowed();
        }
        //check if name and surname match pattern
        String firstNamePattern = "([A-ZÓĄŚŁŻŹĆŃ])([a-zóąśłżźćń]){2,}";
        String lastNamePattern = "([A-ZĘÓĄŚŁŻŹĆŃ])([a-zęóąśłżźćń])*(([-])([A-ZĘÓĄŚŁŻŹĆŃ])([a-zęóąśłżźćń])*)?";
        if (!request.getFirstName().matches(firstNamePattern) || !request.getLastName().matches(lastNamePattern) || request.getLastName().length() < 3) {
            throw new NameNotAllowedException();
        }

        List<SeatDTO> availableSeats = seatRepository.findAvailableSeatsByScreening(request.getScreeningId());
        for (Integer s : request.getSeats()) {

        }

    }

    @GetMapping("/bookings")
    public CollectionModel<EntityModel<Booking>> getAllBookings() {
        List<EntityModel<Booking>> bookings = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(bookings, linkTo(methodOn(BookingController.class).getAllBookings()).withSelfRel());
    }

    @GetMapping("/bookings/{id}")
    public EntityModel<Booking> getBookingById(@PathVariable Long id) {
        Booking booking = repository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
        return assembler.toModel(booking);
    }

}
