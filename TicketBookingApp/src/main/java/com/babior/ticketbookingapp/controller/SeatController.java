package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.assembler.SeatAssembler;
import com.babior.ticketbookingapp.business.entity.Seat;
import com.babior.ticketbookingapp.exception.notfound.SeatNotFoundException;
import com.babior.ticketbookingapp.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
public class SeatController {

    private final SeatRepository repository;
    private final SeatAssembler assembler;

    @GetMapping("/seats")
    public CollectionModel<EntityModel<Seat>> getAllSeats() {
        List<EntityModel<Seat>> seats = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(seats, linkTo(methodOn(SeatController.class).getAllSeats()).withSelfRel());
    }

    @GetMapping("/seats/{id}")
    public EntityModel<Seat> getSeatById(@PathVariable Long id) {
        Seat seat = repository.findById(id).orElseThrow(() -> new SeatNotFoundException(id));
        return assembler.toModel(seat);
    }
}
