package com.babior.ticketbookingapp.business.dto;

import com.babior.ticketbookingapp.business.entity.Seat;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "screening", collectionRelation = "screenings")
public class RoomRepresentation {
    private final String roomName;
    private final List<EntityModel<Seat>> availableSeats;
}
