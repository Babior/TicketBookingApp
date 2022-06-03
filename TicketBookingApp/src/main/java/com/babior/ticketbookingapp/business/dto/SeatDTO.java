package com.babior.ticketbookingapp.business.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Builder
@Data
@Relation(itemRelation = "seat", collectionRelation = "seats")
public class SeatDTO {
    private Long number;
    private Long row;
}
