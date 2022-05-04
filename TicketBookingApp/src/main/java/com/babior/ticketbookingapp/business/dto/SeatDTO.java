package com.babior.ticketbookingapp.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "seat", collectionRelation = "seats")
public class SeatDTO {
    private final Long seatId;
    private final Long seatNumber;
    private final Long seatRow;
    private final Long roomId;
}