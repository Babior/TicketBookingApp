package com.babior.ticketbookingapp.assembler;

import com.babior.ticketbookingapp.business.entity.Booking;
import com.babior.ticketbookingapp.controller.BookingController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookingAssembler implements RepresentationModelAssembler<Booking, EntityModel<Booking>> {
    @Override
    public EntityModel<Booking> toModel(Booking booking) {
        return EntityModel.of(booking,
                linkTo(methodOn(BookingController.class).getBookingById(booking.getId())).withSelfRel(),
                linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"));
    }
}
