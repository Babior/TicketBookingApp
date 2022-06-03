package com.babior.ticketbookingapp.business.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.core.Relation;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "room", collectionRelation = "rooms")
public class RoomRepresentation {
    private final String roomName;
    private final CollectionModel<EntityModel<SeatDTO>> availableSeats;
}
