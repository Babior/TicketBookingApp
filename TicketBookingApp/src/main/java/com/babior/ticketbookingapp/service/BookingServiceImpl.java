package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.BookingAssembler;
import com.babior.ticketbookingapp.business.dto.BookingRepresentation;
import com.babior.ticketbookingapp.business.dto.BookingDTO;
import com.babior.ticketbookingapp.business.entity.*;
import com.babior.ticketbookingapp.exception.EntityNotFoundException;
import com.babior.ticketbookingapp.exception.NotAllowedException;
import com.babior.ticketbookingapp.repository.BookingRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import com.babior.ticketbookingapp.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;

@Component
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingAssembler bookingAssembler;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_DIFFERENCE_BETWEEN_SEATS = 2;
    private static final int MAX_TIME_BEFORE_SCREENING = 15;
    private static final int MAX_TIME_FOR_PAYMENT = 15;


    @NotNull
    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<BookingRepresentation>> findAllBookings() {
        return bookingAssembler.toCollectionModel(bookingRepository.findAll());

    }

    @NotNull
    @Transactional(readOnly = true)
    public EntityModel<BookingRepresentation> findBookingById(@NotNull Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Booking.class.getSimpleName(), id));
        return bookingAssembler.toModel(booking);
    }

    @NotNull
    @Transactional
    public EntityModel<BookingRepresentation> createBooking(@NotNull BookingDTO request) {
        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new EntityNotFoundException(Screening.class.getSimpleName(), request.getScreeningId()));

        if (hasIllegalScreeningTime(screening)) {
            throw new NotAllowedException("Illegal screening time, should be at least 15 in advance");
        }
        if (hasIllegalName(request)) {
            throw new NotAllowedException("Illegal firstname/lastname");
        }
        if (hasEmptySeats(request)) {
            throw new NotAllowedException("Illegal seats array, should contain at least one seat");
        }
        if (!areSeatsAvailable(request)) {
            throw new NotAllowedException("Illegal seats, requested seats are not available");
        }
        List<Seat> requestedSeats = seatRepository.findAllByIdIn(request.getSeats().keySet());
        if (haveSeatInBetween(requestedSeats)) {
            throw new NotAllowedException("Free seat left between booked seats");
        }

        double totalPrice = request.getSeats().values().stream().mapToDouble(TicketType::getPrice).sum();

        var booking = Booking.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .expiryDate(LocalDateTime.now().plusMinutes(MAX_TIME_FOR_PAYMENT))
                .screening(screening)
                .seats(requestedSeats.stream().collect(Collectors.toMap(it -> it, it -> request.getSeats().get(it.getId()))))
                .totalPrice(totalPrice)
                .build();

        return bookingAssembler.toModel(bookingRepository.save(booking));
    }

    private boolean hasIllegalScreeningTime(@NotNull Screening screening) {
        //at latest 15 minutes before the screening begins
        return screening.getStartDate().isBefore(LocalDateTime.now().plusMinutes(MAX_TIME_BEFORE_SCREENING));
    }

    private boolean hasIllegalName(@NotNull BookingDTO request) {
        //check if name and surname match pattern
        String firstNamePattern = "([A-ZÓĄŚŁŻŹĆŃ])([a-zóąśłżźćń]){2,}";
        String lastNamePattern = "([A-ZĘÓĄŚŁŻŹĆŃ])([a-zęóąśłżźćń])*(([-])([A-ZĘÓĄŚŁŻŹĆŃ])([a-zęóąśłżźćń])*)?";
        return !request.getFirstName().matches(firstNamePattern) || !request.getLastName().matches(lastNamePattern)
                || request.getFirstName().length() < MIN_NAME_LENGTH || request.getLastName().length() < MIN_NAME_LENGTH;
    }

    private boolean hasEmptySeats(@NotNull BookingDTO request) {
        //check if reservation has at least a single seat
        return CollectionUtils.isEmpty(request.getSeats());
    }

    private boolean areSeatsAvailable(@NotNull BookingDTO request) {
        var available = seatRepository.findAvailableSeatsByScreening(request.getScreeningId()).stream()
                .collect(Collectors.toMap(Seat::getId, Function.identity()));
        return available.keySet().containsAll(request.getSeats().keySet());
    }

    private boolean haveSeatInBetween(List<Seat> requestedSeats) {
        //check is there is no free seat between booked seats
        Map<Long, List<Long>> seatsMap = requestedSeats
                .stream()
                .collect(Collectors.groupingBy(Seat::getRow, mapping(Seat::getNumber, Collectors.toList())));

        seatsMap.forEach((key, value) -> Collections.sort(value));
        for (Map.Entry<Long, List<Long>> me : seatsMap.entrySet()) {
            int i = 0;
            int j = 1;
            while (i < me.getValue().size() && j < me.getValue().size()) {
                if (i != j && me.getValue().get(j) - me.getValue().get(i) == MAX_DIFFERENCE_BETWEEN_SEATS) {
                    return true;
                }
                //if difference here isn't equal to given difference move pointers
                else {
                    i++;
                    j++;
                }
            }
        }
        return false;
    }

    @NotNull
    @Transactional
    public EntityModel<BookingRepresentation> saveOrUpdateBooking(BookingDTO newBooking, @NotNull Long id) {
        Screening screening = screeningRepository.findById(newBooking.getScreeningId())
                .orElseThrow(() -> new EntityNotFoundException(Screening.class.getSimpleName(), newBooking.getScreeningId()));
        if (hasIllegalName(newBooking)) {
            throw new NotAllowedException("Illegal firstname/lastname");
        }
        if (hasEmptySeats(newBooking)) {
            throw new NotAllowedException("Illegal seats array, should contain at least one seat");
        }
        if (!areSeatsAvailable(newBooking)) {
            throw new NotAllowedException("Illegal seats, requested seats are not available");
        }
        List<Seat> requestedSeats = seatRepository.findAllByIdIn(newBooking.getSeats().keySet());
        if (haveSeatInBetween(requestedSeats)) {
            throw new NotAllowedException("Free seat left between booked seats");
        }        double totalPrice = newBooking.getSeats().values().stream().mapToDouble(TicketType::getPrice).sum();
        Booking updatedBooking = bookingRepository.findById(id)
                .map(booking -> {
                    booking.setFirstName(newBooking.getFirstName());
                    booking.setLastName(newBooking.getLastName());
                    booking.setScreening(screening);
                    booking.setSeats(requestedSeats
                            .stream()
                            .collect(Collectors.toMap(it -> it, it -> newBooking.getSeats().get(it.getId()))));
                    booking.setTotalPrice(totalPrice);
                    booking.setExpiryDate(LocalDateTime.now().plusMinutes(MAX_TIME_FOR_PAYMENT));
                    return bookingRepository.save(booking);
                }).orElseGet(() -> bookingRepository.save(Booking.builder()
                        .id(id)
                        .firstName(newBooking.getFirstName())
                        .lastName(newBooking.getLastName())
                        .screening(screening)
                        .seats(requestedSeats
                                .stream()
                                .collect(Collectors.toMap(it -> it, it -> newBooking.getSeats().get(it.getId()))))
                        .totalPrice(totalPrice)
                        .expiryDate(LocalDateTime.now().plusMinutes(MAX_TIME_FOR_PAYMENT))
                        .build()));
        return bookingAssembler.toModel(updatedBooking);
    }

    @Transactional
    public void deleteBooking(@NotNull Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Booking.class.getSimpleName(), id));
        bookingRepository.delete(booking);
    }

    @Transactional
    public void deleteByScreening(@NotNull Screening screening) {
        var bookingIds = bookingRepository.findBookingsByScreening(screening)
                .stream().map(Booking::getId).collect(Collectors.toSet());
        bookingRepository.deleteByIdIn(bookingIds);
    }

    @Transactional
    public void deleteBySeat(@NotNull Long seatId) {
        var bookingIds = bookingRepository
                .findBookingsBySeatId(seatId).stream().map(Booking::getId).collect(Collectors.toSet());
        bookingRepository.deleteByIdIn(bookingIds);
    }
}
