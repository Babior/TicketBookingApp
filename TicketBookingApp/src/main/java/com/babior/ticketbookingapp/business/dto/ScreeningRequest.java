package com.babior.ticketbookingapp.business.dto;

import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScreeningRequest {
    private Long movieId;
    private Long roomId;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
    private LocalDateTime startDate;

    public Screening convertToEntity(Movie movie, Room room) {
        Screening entity = new Screening();
        entity.setMovie(movie);
        entity.setRoom(room);
        entity.setStartDate(startDate);
        return entity;
    }
}
