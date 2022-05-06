package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.ScreeningAssembler;
import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.dto.ScreeningRequest;
import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.exception.EntityNotFoundException;
import com.babior.ticketbookingapp.repository.MovieRepository;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScreeningService {

    private final ScreeningRepository repository;
    private final ScreeningAssembler assembler;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    private final Sort defaultSort = Sort.by("movieTitle", "startDate").ascending();

    @NotNull
    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<ScreeningRepresentation>> findAllScreenings() {
        return assembler.toCollectionModel(repository.findAll());
    }

    @NotNull
    @Transactional(readOnly = true)
    public EntityModel<ScreeningRepresentation> findScreeningById(@NotNull Long id) {
        return repository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new EntityNotFoundException(Screening.class.getSimpleName(), id));
    }

    @NotNull
    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<ScreeningRepresentation>> findScreeningsAfterDate(@NotNull LocalDateTime dateTime) {
        return assembler.toCollectionModel(repository.findScreeningsByStartDateIsAfter(dateTime, defaultSort));
    }

    @NotNull
    @Transactional(readOnly = true)
    public String findRoomNameByScreening(@NotNull Long id) {
        Screening screening = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Screening.class.getSimpleName(), id));
        return screening.getRoom().getName();
    }

    @NotNull
    @Transactional(readOnly = true)
    public EntityModel<ScreeningRepresentation> createScreening(ScreeningRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException(Movie.class.getSimpleName(), request.getMovieId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException(Room.class.getSimpleName(), request.getRoomId()));

        Screening newScreening = request.convertToEntity(movie, room);

        return assembler.toModel(repository.save(newScreening));
    }

    @NotNull
    @Transactional(readOnly = true)
    public void deleteByMovie(Movie movie) {
        List<Screening> screenings = repository.findScreeningsByMovie(movie);
        for (Screening s : screenings) {
            repository.deleteById(s.getId());
        }

        var screeningIds = repository.findScreeningsByMovie(movie)
                .stream().map(Screening::getId).collect(Collectors.toSet());

        repository.deleteByIdIn(screeningIds);
    }

    @NotNull
    @Transactional(readOnly = true)
    public void deleteByRoom(Room room) {
        List<Screening> screenings = repository.findScreeningsByRoom(room);
        for (Screening s : screenings) {
            repository.deleteById(s.getId());
        }
    }
}
