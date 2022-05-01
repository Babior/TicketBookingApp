package com.babior.ticketbookingapp.controller;

import com.babior.ticketbookingapp.assembler.RoomAssembler;
import com.babior.ticketbookingapp.assembler.ScreeningAssembler;
import com.babior.ticketbookingapp.business.Movie;
import com.babior.ticketbookingapp.business.Room;
import com.babior.ticketbookingapp.business.Screening;
import com.babior.ticketbookingapp.exception.notfound.MovieNotFoundException;
import com.babior.ticketbookingapp.exception.notfound.ScreeningNotFoundException;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
public class ScreeningController {

    private ScreeningRepository repository;
    private ScreeningAssembler assembler;
    private RoomRepository roomRepository;


    @GetMapping("/screenings")
    public CollectionModel<EntityModel<Screening>> findAllScreenings() {
        List<EntityModel<Screening>> screenings = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(screenings, linkTo(methodOn(ScreeningController.class).findAllScreenings()).withSelfRel());
    }

    @GetMapping("/screenings/{id}")
    public EntityModel<Screening> findMScreeningById(@PathVariable Long id) {
        Screening screening = repository.findById(id).orElseThrow(() -> new ScreeningNotFoundException(id));
        return assembler.toModel(screening);
    }

    @DeleteMapping("/screenings/{id}")
    public ResponseEntity<?> deleteScreening(@PathVariable Long id) {
        return null;
    }


}
