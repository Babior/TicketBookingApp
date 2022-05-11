package com.babior.ticketbookingapp.business.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Builder
@Data
@Relation(itemRelation = "movie", collectionRelation = "movies")
public class MovieDTO {
    private String title;
    private int runningTime;
}
