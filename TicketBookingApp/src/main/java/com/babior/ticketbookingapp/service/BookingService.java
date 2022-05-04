package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.BookingAssembler;
import com.babior.ticketbookingapp.business.dto.BookingDTO;
import com.babior.ticketbookingapp.business.entity.Booking;
import com.babior.ticketbookingapp.business.entity.BookingSeat;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.business.entity.Seat;
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

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
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

    public List<EntityModel<Booking>> findAllBookings() {
        return repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Booking> findBookingById(@NotNull Long id) {
        Booking booking = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Booking.class.getSimpleName(), id));
        return assembler.toModel(booking);
    }

    public EntityModel<Booking> createBooking(@NotNull BookingDTO request) {
        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new EntityNotFoundException(Screening.class.getSimpleName(), request.getScreeningId()));
        //at latest 15 minutes before the screening begins
        if (screening.getStartDate().isBefore(LocalDateTime.now().plusMinutes(15))) {
            throw new TimeNotAllowedException();
        }
        //check if reservation has at least a single seat
        if (request.getSeats() == null || request.getSeats().size() == 0) {
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

        if (!available.keySet().containsAll(request.getSeats())) {
            // TODO: поменять not allowed exceptions на generic как с entity not found
            throw new NoSeatsNotAllowed();
        }

        var booking = Booking.builder()
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .screening(screening)
                //.ticketType(TicketType.ADULT)
                .totalPrice(12.0)
                //.seats(available.entrySet().stream().filter(it -> request.getSeats().contains(it.getKey())).map(Map.Entry::getValue).collect(Collectors.toList()))
                .build();

        for (Seat s : seatRepository.findAllById(request.getSeats())) {
            var bookingSeat = BookingSeat.builder()
                    .seat(s)
                    .booking(booking)
                    .ticketType(request.getTicketType())
                    .build();
            bookingSeatRepository.save(bookingSeat);
        }

        return assembler.toModel(repository.save(booking));
    }
}
