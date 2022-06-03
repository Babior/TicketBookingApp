package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.dto.BookingRepresentation;
import com.babior.ticketbookingapp.business.dto.SeatDTO;
import com.babior.ticketbookingapp.business.entity.Booking;
import com.babior.ticketbookingapp.business.entity.Seat;
import com.babior.ticketbookingapp.controller.BookingController;
import com.babior.ticketbookingapp.controller.MovieController;
import com.babior.ticketbookingapp.controller.ScreeningController;
import com.babior.ticketbookingapp.controller.SeatController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookingAssembler implements RepresentationModelAssembler<Booking, EntityModel<BookingRepresentation>> {
    @Override
    public EntityModel<BookingRepresentation> toModel(Booking entity) {
        BookingRepresentation bookingRepresentation = BookingRepresentation.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .totalPrice(entity.getTotalPrice())
                .expirationDate(entity.getExpiryDate())
                .build();

        return EntityModel.of(bookingRepresentation,
                linkTo(methodOn(BookingController.class).getBookingById(entity.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<BookingRepresentation>> toCollectionModel(Iterable<? extends Booking> entities) {
        CollectionModel<EntityModel<BookingRepresentation>> bookingsRepresentation = RepresentationModelAssembler.super.toCollectionModel(entities);
        bookingsRepresentation.add(linkTo(methodOn(BookingController.class).getAllBookings()).withSelfRel());
        return bookingsRepresentation;
    }
}
