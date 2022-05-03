package com.babior.ticketbookingapp.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "seat", collectionRelation = "seats")
public class SeatDTO {
    private final Long seatNumber;
    private final Long seatRow;
    private final Long roomId;

    public SeatDTO(Long seatNumber, Long seatRow, Long roomId) {
        this.seatNumber = seatNumber;
        this.seatRow = seatRow;
        this.roomId = roomId;
    }
}