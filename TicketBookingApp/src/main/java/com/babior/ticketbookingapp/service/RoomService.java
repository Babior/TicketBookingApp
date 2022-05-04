package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.RoomAssembler;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.controller.ScreeningController;
import com.babior.ticketbookingapp.exception.EntityNotFoundException;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomService {

    private RoomRepository repository;
    private RoomAssembler assembler;
    private ScreeningService screeningService;

    public List<EntityModel<Room>> findAllRooms() {
        return repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Room> findRoomById(@NotNull Long id) {
        Room room = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Room.class.getSimpleName(), id));
        return assembler.toModel(room);
    }

    public void deleteRoom(@NotNull Long id) {
        Room room = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Room.class.getSimpleName(), id));
        screeningService.deleteByRoom(room);
        repository.deleteById(id);
    }


}
