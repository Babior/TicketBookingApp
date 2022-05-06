package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.BookingAssembler;
import com.babior.ticketbookingapp.business.dto.BookingRequest;
import com.babior.ticketbookingapp.business.entity.*;
import com.babior.ticketbookingapp.exception.EntityNotFoundException;
import com.babior.ticketbookingapp.exception.notallowed.NameNotAllowedException;
import com.babior.ticketbookingapp.exception.notallowed.NoSeatsNotAllowed;
import com.babior.ticketbookingapp.exception.notallowed.TimeNotAllowedException;
import com.babior.ticketbookingapp.repository.BookingRepository;
import com.babior.ticketbookingapp.repository.BookingSeatRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import com.babior.ticketbookingapp.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookingService {
    private final BookingRepository repository;
    private final BookingAssembler assembler;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;

    @NotNull
    @Transactional(readOnly = true)
    public List<EntityModel<Booking>> findAllBookings() {
        return repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    @NotNull
    @Transactional(readOnly = true)
    public EntityModel<Booking> findBookingById(@NotNull Long id) {
        Booking booking = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Booking.class.getSimpleName(), id));
        return assembler.toModel(booking);
    }

    @NotNull
    @Transactional(readOnly = true)
    public EntityModel<Booking> createBooking(@NotNull BookingRequest request) {
        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new EntityNotFoundException(Screening.class.getSimpleName(), request.getScreeningId()));
        //at latest 15 minutes before the screening begins
        if (screening.getStartDate().isBefore(LocalDateTime.now().plusMinutes(15))) {
            throw new TimeNotAllowedException();
        }
        //check if reservation has at least a single seat
        if (CollectionUtils.isEmpty(request.getSeats())) {
            throw new NoSeatsNotAllowed();
        }
        //check if name and surname match pattern
        String firstNamePattern = "([A-ZÓĄŚŁŻŹĆŃ])([a-zóąśłżźćń]){2,}";
        String lastNamePattern = "([A-ZĘÓĄŚŁŻŹĆŃ])([a-zęóąśłżźćń])*(([-])([A-ZĘÓĄŚŁŻŹĆŃ])([a-zęóąśłżźćń])*)?";
        if (!request.getFirstName().matches(firstNamePattern) || !request.getLastName().matches(lastNamePattern) || request.getLastName().length() < 3) {
            throw new NameNotAllowedException();
        }

        var available = seatRepository.findAvailableSeatsByScreening(request.getScreeningId()).stream()
                .collect(Collectors.toMap(Seat::getId, Function.identity()));

        if (!available.keySet().containsAll(request.getSeats().keySet())) {
            // TODO: поменять not allowed exceptions на generic как с entity not found
            throw new NoSeatsNotAllowed();
        }

        List<Seat> requestedSeats = seatRepository.findAllByIdIn(request.getSeats().keySet());

        // TODO: Как создать booking без total price, потом создать все связи booking-seat, потом посчитать total price
        // и добавить в этот букинг
        var booking = Booking.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .screening(screening)
                .build();

        Map<Seat, TicketType> result = new HashMap<>();
        requestedSeats.forEach(seat -> result.put(seat, request.getSeats().get(seat.getId())));

        result.forEach((key, value) -> {
            var bookingSeat = BookingSeat.builder()
                    .seat(key)
                    .booking(booking)
                    .ticketType(value)
                    .build();
            bookingSeatRepository.save(bookingSeat);
        });

        return assembler.toModel(repository.save(booking));
    }
}
