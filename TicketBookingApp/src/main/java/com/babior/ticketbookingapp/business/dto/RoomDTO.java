package com.babior.ticketbookingapp.business.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Builder
@Data
@Relation(itemRelation = "room", collectionRelation = "rooms")
public class RoomDTO {
    private String name;
    private Set<Long> seatIds;
}
