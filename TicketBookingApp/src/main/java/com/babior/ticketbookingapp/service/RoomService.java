package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.business.dto.RoomDTO;
import com.babior.ticketbookingapp.business.dto.RoomRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import javax.validation.constraints.NotNull;

public interface RoomService {
    @NotNull
    CollectionModel<EntityModel<RoomRepresentation>> findAllRooms();

    @NotNull
    EntityModel<RoomRepresentation> findRoomById(@NotNull Long id);

    @NotNull
    EntityModel<RoomRepresentation> createRoom(RoomDTO newRoom);

    @NotNull
    EntityModel<RoomRepresentation> saveOrUpdateRoom(RoomDTO newRoom, @NotNull Long id);

    void deleteRoom(@NotNull Long id);
}
