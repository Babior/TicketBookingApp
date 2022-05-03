package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.assembler.SeatAssembler;
import com.babior.ticketbookingapp.assembler.ScreeningAssembler;
import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.dto.SeatDTO;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.business.dto.ScreeningRequest;
import com.babior.ticketbookingapp.business.entity.Seat;
import com.babior.ticketbookingapp.business.view.ScreeningView;
import com.babior.ticketbookingapp.exception.notfound.MovieNotFoundException;
import com.babior.ticketbookingapp.exception.notfound.RoomNotFoundException;
import com.babior.ticketbookingapp.exception.notfound.ScreeningNotFoundException;
import com.babior.ticketbookingapp.repository.MovieRepository;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import com.babior.ticketbookingapp.repository.SeatRepository;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class ScreeningController {

    private ScreeningRepository repository;
    //private ScreeningAssembler assembler;
    private ScreeningAssembler representationAssembler;
    private SeatAssembler seatAssembler;
    private SeatRepository seatRepository;
    private RoomRepository roomRepository;
    private MovieRepository movieRepository;

    @GetMapping("/screenings")
    @JsonView(ScreeningView.ResponseView.class)
    public ResponseEntity<CollectionModel<EntityModel<ScreeningRepresentation>>> getAllScreenings() throws JsonProcessingException {
        return ResponseEntity.ok(representationAssembler.toCollectionModel(repository.findAll(Sort.by("startDate"))));
    }

    @GetMapping(value = "/screenings/{id}")
    public ResponseEntity<EntityModel<ScreeningRepresentation>> getScreeningById(@PathVariable Long id) {
        return repository.findById(id)
                .map(screening -> {
                    EntityModel<ScreeningRepresentation> screeningRepresentation = representationAssembler.toModel(screening);
                    return ResponseEntity.ok(screeningRepresentation);
                })
                .orElseThrow(() -> new ScreeningNotFoundException(id));
    }

    @GetMapping("/screenings/after/{dateTimeString}")
    public ResponseEntity<CollectionModel<EntityModel<ScreeningRepresentation>>> getAllScreeningsStartingFrom(@PathVariable String dateTimeString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        if (dateTime.isBefore(LocalDateTime.now().plusMinutes(15))) {
            dateTime = LocalDateTime.now().plusMinutes(15);
        }

        return ResponseEntity.ok(representationAssembler.toCollectionModel(repository.findScreeningsByStartDateIsAfter(dateTime, Sort.by("movieTitle", "startDate"))));
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
    public ResponseEntity<List<SeatDTO>> getSeatsInScreening(@PathVariable Long screeningId) {
        List<SeatDTO> test = seatRepository.findAvailableSeatsByScreening(screeningId);

        return ResponseEntity.ok(test);
    }

    @PostMapping("/screenings")
    public ResponseEntity<?> addScreening(@RequestBody ScreeningRequest request) {
        // TODO: Response with not found instead of custom exception
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException(request.getMovieId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(request.getRoomId()));

        Screening newScreening = request.convertToEntity(movie, room);
        EntityModel<ScreeningRepresentation> screeningRepresentation = representationAssembler.toModel(repository.save(newScreening));
        return ResponseEntity
                .created(screeningRepresentation.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(screeningRepresentation);
    }

    @DeleteMapping("/screenings/{id}")
    public ResponseEntity<?> deleteScreening(@PathVariable Long id) {
        return null;
    }
}
