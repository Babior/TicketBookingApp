package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.assembler.ScreeningAssembler;
import com.babior.ticketbookingapp.assembler.SeatAssembler;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.business.dto.ScreeningDTO;
import com.babior.ticketbookingapp.business.entity.Seat;
import com.babior.ticketbookingapp.exception.notfound.MovieNotFoundException;
import com.babior.ticketbookingapp.exception.notfound.RoomNotFoundException;
import com.babior.ticketbookingapp.exception.notfound.ScreeningNotFoundException;
import com.babior.ticketbookingapp.repository.MovieRepository;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
public class ScreeningController {

    private ScreeningRepository repository;
    private ScreeningAssembler assembler;
    private SeatAssembler seatAssembler;
    private RoomRepository roomRepository;
    private MovieRepository movieRepository;

    @GetMapping("/screenings")
    public CollectionModel<EntityModel<Screening>> getAllScreenings() {
        List<EntityModel<Screening>> screenings = repository.findAll(Sort.by(Sort.Direction.ASC, "movie")).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(screenings, linkTo(methodOn(ScreeningController.class)
                .getAllScreenings()).withSelfRel());
    }

    @GetMapping("/screenings/{id}")
    public EntityModel<Screening> getScreeningById(@PathVariable Long id) {
        Screening screening = repository.findById(id).orElseThrow(() -> new ScreeningNotFoundException(id));
        return assembler.toModel(screening);
    }

    @GetMapping("/screenings/after/{dateTimeString}")
    //@JsonView(ScreeningView.ResponseView.class)
    public CollectionModel<EntityModel<Screening>> getAllScreeningsStartingFrom(@PathVariable String dateTimeString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        if (dateTime.isBefore(LocalDateTime.now().plusMinutes(15))) {
            dateTime = LocalDateTime.now().plusMinutes(15);
        }

        List<EntityModel<Screening>> screenings = repository.findScreeningsByStartDateIsAfter(dateTime).stream()
                .sorted()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(screenings, linkTo(methodOn(ScreeningController.class)
                .getAllScreeningsStartingFrom(dateTimeString)).withSelfRel());
    }

//    @GetMapping(path = "/screenings/{screeningId}/seats")
//    public CollectionModel<EntityModel<Seat>> getSeatsInScreening(@PathVariable Long screeningId) {
//        Screening screening = repository.findById(screeningId)
//                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
//        List<EntityModel<Seat>> seats = screening.getRoom().getSeats().stream()
//                .map(seatAssembler::toModel)
//                .collect(Collectors.toList());
//
//        return CollectionModel.of(seats, linkTo(methodOn(ScreeningController.class)
//                .getSeatsInScreening(screeningId)).withSelfRel());
//    }

    @GetMapping(path = "/screenings/{screeningId}/seats")
    public ResponseEntity<List<Object>> getSeatsInScreening(@PathVariable Long screeningId) {

        List<Object> list = repository.findAvailableSeats();

        return ResponseEntity.ok(list);
    }

    @PostMapping("/screenings")
    public ResponseEntity<?> addScreening(@RequestBody ScreeningDTO request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException(request.getMovieId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(request.getRoomId()));

        Screening newScreening = request.convertToEntity(movie, room);
        EntityModel<Screening> entityModel = assembler.toModel(repository.save(newScreening));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/screenings/{id}")
    public ResponseEntity<?> deleteScreening(@PathVariable Long id) {
        return null;
    }
}
