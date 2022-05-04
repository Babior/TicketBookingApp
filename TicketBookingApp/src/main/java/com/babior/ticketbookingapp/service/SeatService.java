package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.SeatAssembler;
import com.babior.ticketbookingapp.business.dto.RoomRepresentation;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Seat;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SeatService {
    private final SeatRepository repository;
    private final SeatAssembler assembler;

    public List<EntityModel<Seat>> findAvailableSeatsByScreening(@NotNull Long screeningId) {
        List<Seat> seats = repository.findAvailableSeatsByScreening(screeningId);
        return seats.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

}
