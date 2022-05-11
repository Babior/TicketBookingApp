package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.SeatAssembler;
import com.babior.ticketbookingapp.business.dto.SeatDTO;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Seat;
import com.babior.ticketbookingapp.exception.EntityNotFoundException;
import com.babior.ticketbookingapp.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@AllArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final SeatAssembler seatAssembler;
    private final BookingServiceImpl bookingService;

    private final Sort defaultSort = Sort.by("row", "number").ascending();


    @NotNull
    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<SeatDTO>> findAllSeats() {
        return seatAssembler.toCollectionModel(seatRepository.findAll(defaultSort));
    }

    @NotNull
    @Transactional(readOnly = true)
    public EntityModel<SeatDTO> findSeatById(@NotNull Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Seat.class.getSimpleName(), id));
        return seatAssembler.toModel(seat);
    }

    @NotNull
    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<SeatDTO>> findAvailableSeatsByScreening(@NotNull Long screeningId) {
        List<Seat> seats = seatRepository.findAvailableSeatsByScreening(screeningId);
        return seatAssembler.toCollectionModel(seats);
    }

    @NotNull
    @Transactional
    public EntityModel<SeatDTO> createSeat(SeatDTO newSeat) {
        Seat seat = Seat.builder()
                .row(newSeat.getRow())
                .number(newSeat.getNumber())
                .build();
        return seatAssembler.toModel(seatRepository.save(seat));
    }

    @NotNull
    @Transactional
    public EntityModel<SeatDTO> saveOrUpdateSeat(SeatDTO newSeat, @NotNull Long id) {
        Seat updatedSeat = seatRepository.findById(id)
                .map(seat -> {
                    seat.setNumber(newSeat.getNumber());
                    seat.setRow(newSeat.getRow());
                    return seatRepository.save(seat);
                }).orElseGet(() -> seatRepository.save(Seat.builder()
                        .id(id)
                        .row(newSeat.getRow())
                        .number(newSeat.getNumber())
                        .build()));
        return seatAssembler.toModel(updatedSeat);
    }

    @Transactional
    public void deleteSeat(@NotNull Long id) {
        seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Seat.class.getSimpleName(), id));
        bookingService.deleteBySeat(id);
        seatRepository.deleteById(id);
    }

}
