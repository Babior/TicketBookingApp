package com.babior.ticketbookingapp.service;

import com.babior.ticketbookingapp.assembler.ScreeningAssembler;
import com.babior.ticketbookingapp.business.dto.ScreeningRepresentation;
import com.babior.ticketbookingapp.business.dto.ScreeningDTO;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final ScreeningAssembler screeningAssembler;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final BookingServiceImpl bookingService;

    private final Sort defaultSort = Sort.by("movieTitle", "startDate").ascending();

    @NotNull
    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<ScreeningRepresentation>> findAllScreenings() {
        return screeningAssembler.toCollectionModel(screeningRepository.findAll());
    }

    @NotNull
    @Transactional(readOnly = true)
    public EntityModel<ScreeningRepresentation> findScreeningById(@NotNull Long id) {
        return screeningRepository.findById(id)
                .map(screeningAssembler::toModel)
                .orElseThrow(() -> new EntityNotFoundException(Screening.class.getSimpleName(), id));
    }

    @NotNull
    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<ScreeningRepresentation>> findScreeningsAfterDate(@NotNull LocalDateTime dateTime) {
        return screeningAssembler.toCollectionModel(screeningRepository.findScreeningsByStartDateIsAfter(dateTime, defaultSort));
    }

    @NotNull
    @Transactional(readOnly = true)
    public String findRoomNameByScreening(@NotNull Long id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Screening.class.getSimpleName(), id));
        return screening.getRoom().getName();
    }

    @NotNull
    @Transactional
    public EntityModel<ScreeningRepresentation> createScreening(ScreeningDTO request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException(Movie.class.getSimpleName(), request.getMovieId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException(Room.class.getSimpleName(), request.getRoomId()));

        Screening newScreening = Screening.builder()
                .movie(movie)
                .room(room)
                .startDate(request.getStartDate())
                .build();

        return screeningAssembler.toModel(screeningRepository.save(newScreening));
    }

    @NotNull
    @Transactional
    public EntityModel<ScreeningRepresentation> saveOrUpdateScreening(ScreeningDTO newScreening, @NotNull Long id) {
        Movie movie = movieRepository.findById(newScreening.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException(Movie.class.getSimpleName(), newScreening.getMovieId()));
        Room room = roomRepository.findById(newScreening.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException(Room.class.getSimpleName(), newScreening.getRoomId()));
        Screening updatedScreening = screeningRepository.findById(id)
                .map(screening -> {
                    screening.setMovie(movie);
                    screening.setRoom(room);
                    screening.setStartDate(newScreening.getStartDate());
                    return screeningRepository.save(screening);
                }).orElseGet(() -> screeningRepository.save(Screening.builder()
                        .id(id)
                        .movie(movie)
                        .room(room)
                        .startDate(newScreening.getStartDate())
                        .build()));
        return screeningAssembler.toModel(updatedScreening);
    }

    @Transactional
    public void deleteScreening(@NotNull Long id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Screening.class.getSimpleName(), id));
        bookingService.deleteByScreening(screening);
        screeningRepository.deleteById(id);
    }

    @Transactional
    public void deleteByMovie(@NotNull Movie movie) {
        var screeningIds = screeningRepository.findScreeningsByMovie(movie)
                .stream().map(Screening::getId).collect(Collectors.toSet());

        screeningRepository.deleteByIdIn(screeningIds);
    }

    @Transactional
    public void deleteByRoom(@NotNull Room room) {
        var screeningIds = screeningRepository.findScreeningsByRoom(room)
                .stream().map(Screening::getId).collect(Collectors.toSet());

        screeningRepository.deleteByIdIn(screeningIds);
    }
}
