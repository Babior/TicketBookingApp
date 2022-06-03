package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.business.dto.SeatDTO;
import com.babior.ticketbookingapp.business.entity.Seat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import javax.validation.constraints.NotNull;

public interface SeatService {
    @NotNull
    CollectionModel<EntityModel<SeatDTO>> findAllSeats();

    @NotNull
    EntityModel<SeatDTO> findSeatById(@NotNull Long id);

    @NotNull
    CollectionModel<EntityModel<SeatDTO>> findAvailableSeatsByScreening(@NotNull Long screeningId);

    @NotNull
    EntityModel<SeatDTO> createSeat(SeatDTO newSeat);

    @NotNull
    EntityModel<SeatDTO> saveOrUpdateSeat(SeatDTO newSeat, @NotNull Long id);

    void deleteSeat(@NotNull Long id);
}
