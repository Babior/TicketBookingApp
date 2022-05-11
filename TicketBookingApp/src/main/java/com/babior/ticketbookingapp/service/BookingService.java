package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.business.dto.BookingRepresentation;
import com.babior.ticketbookingapp.business.dto.BookingDTO;
import com.babior.ticketbookingapp.business.entity.Screening;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import javax.validation.constraints.NotNull;


public interface BookingService {
    @NotNull
    CollectionModel<EntityModel<BookingRepresentation>> findAllBookings();

    @NotNull
    EntityModel<BookingRepresentation> findBookingById(@NotNull Long id);

    @NotNull
    EntityModel<BookingRepresentation> createBooking(@NotNull BookingDTO request);

    @NotNull
    EntityModel<BookingRepresentation> saveOrUpdateBooking(BookingDTO newBooking, @NotNull Long id);

    void deleteBooking(@NotNull Long id);

    void deleteByScreening(@NotNull Screening screening);

    void deleteBySeat(@NotNull Long seatId);
}
