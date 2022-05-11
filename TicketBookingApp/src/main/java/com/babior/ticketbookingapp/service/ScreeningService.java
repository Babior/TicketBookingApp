package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.dto.ScreeningDTO;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Room;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public interface ScreeningService {
    @NotNull
    CollectionModel<EntityModel<ScreeningRepresentation>> findAllScreenings();

    @NotNull
    EntityModel<ScreeningRepresentation> findScreeningById(@NotNull Long id);

    @NotNull
    CollectionModel<EntityModel<ScreeningRepresentation>> findScreeningsAfterDate(@NotNull LocalDateTime dateTime);

    @NotNull
    String findRoomNameByScreening(@NotNull Long id);

    @NotNull
    EntityModel<ScreeningRepresentation> createScreening(ScreeningDTO request);

    @NotNull
    EntityModel<ScreeningRepresentation> saveOrUpdateScreening(ScreeningDTO newScreening, @NotNull Long id);

    void deleteScreening(@NotNull Long id);

    void deleteByMovie(@NotNull Movie movie);

    void deleteByRoom(@NotNull Room room);
}
