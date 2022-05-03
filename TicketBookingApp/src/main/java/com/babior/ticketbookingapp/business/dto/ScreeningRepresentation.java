package com.babior.ticketbookingapp.business.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "screening", collectionRelation = "screenings")
public class ScreeningRepresentation {
    private final String movieTitle;
    private final LocalDateTime startDate;
}

