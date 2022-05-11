package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.RoomAssembler;
import com.babior.ticketbookingapp.business.dto.RoomDTO;
import com.babior.ticketbookingapp.business.dto.RoomRepresentation;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.exception.EntityNotFoundException;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final RoomAssembler roomAssembler;
    private final ScreeningService screeningService;

    @NotNull
    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<RoomRepresentation>> findAllRooms() {
        return roomAssembler.toCollectionModel(roomRepository.findAll());
    }

    @NotNull
    @Transactional(readOnly = true)
    public EntityModel<RoomRepresentation> findRoomById(@NotNull Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Room.class.getSimpleName(), id));
        return roomAssembler.toModel(room);
    }

    @NotNull
    @Transactional
    public EntityModel<RoomRepresentation> createRoom(RoomDTO newRoom) {
        Room room = Room.builder()
                .name(newRoom.getName())
                .seats(seatRepository.findAllByIdIn(newRoom.getSeatIds()))
                .build();
        return roomAssembler.toModel(roomRepository.save(room));
    }

    @NotNull
    @Transactional
    public EntityModel<RoomRepresentation> saveOrUpdateRoom(RoomDTO newRoom, @NotNull Long id) {
        Room updatedRoom = roomRepository.findById(id)
                .map(room -> {
                    room.setName(newRoom.getName());
                    room.setSeats(seatRepository.findAllByIdIn(newRoom.getSeatIds()));
                    return roomRepository.save(room);
                }).orElseGet(() -> roomRepository.save(Room.builder()
                        .id(id)
                        .name(newRoom.getName())
                        .seats(seatRepository.findAllByIdIn(newRoom.getSeatIds()))
                        .build()));
        return roomAssembler.toModel(updatedRoom);
    }

    @Transactional
    public void deleteRoom(@NotNull Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Room.class.getSimpleName(), id));
        screeningService.deleteByRoom(room);
        roomRepository.deleteById(id);
    }


}
