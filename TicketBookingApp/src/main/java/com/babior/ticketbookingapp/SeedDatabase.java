package com.babior.ticketbookingapp;

import com.babior.ticketbookingapp.business.entity.Movie;
import com.babior.ticketbookingapp.business.entity.Room;
import com.babior.ticketbookingapp.business.entity.Screening;
import com.babior.ticketbookingapp.business.entity.Seat;
import com.babior.ticketbookingapp.repository.MovieRepository;
import com.babior.ticketbookingapp.repository.RoomRepository;
import com.babior.ticketbookingapp.repository.ScreeningRepository;
import com.babior.ticketbookingapp.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
@Transactional
public class SeedDatabase {
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void LoadDatabase() {
        var movie1 = movieRepository.save(Movie.builder().title("The Shawshank Redemption").runningTime(160).build());
        var movie2 = movieRepository.save(Movie.builder().title("Schindler's List").runningTime(130).build());
        var movie3= movieRepository.save(Movie.builder().title("Pulp Fiction").runningTime(120).build());

        var room1 = roomRepository.save(Room.builder()
                .name("South")
                .seats(createSeats())
                .build());

        var room2 = roomRepository.save(Room.builder()
                .name("North")
                .seats(createSeats())
                .build());

        var room3 = roomRepository.save(Room.builder()
                .name("West")
                .seats(createSeats())
                .build());

        screeningRepository.save(Screening.builder()
                .movie(movie1)
                .room(room1)
                .startDate(LocalDateTime.now().plusDays(1))
                .build());

        screeningRepository.save(Screening.builder()
                .movie(movie2)
                .room(room1)
                .startDate(LocalDateTime.now().plusDays(2))
                .build());

        screeningRepository.save(Screening.builder()
                .movie(movie2)
                .room(room2)
                .startDate(LocalDateTime.now().plusDays(1))
                .build());

        screeningRepository.save(Screening.builder()
                .movie(movie3)
                .room(room2)
                .startDate(LocalDateTime.now())
                .build());

        screeningRepository.save(Screening.builder()
                .movie(movie1)
                .room(room3)
                .startDate(LocalDateTime.now())
                .build());

        screeningRepository.save(Screening.builder()
                .movie(movie3)
                .room(room3)
                .startDate(LocalDateTime.now())
                .build());
    }

    private List<Seat> createSeats() {
        var seats = seatRepository.saveAll(List.of(
                Seat.builder()
                        .row(1L)
                        .number(1L)
                        .build(),
                Seat.builder()
                        .row(1L)
                        .number(2L)
                        .build(),
                Seat.builder()
                        .row(1L)
                        .number(3L)
                        .build(),
                Seat.builder()
                        .row(1L)
                        .number(4L)
                        .build(),
                Seat.builder()
                        .row(2L)
                        .number(1L)
                        .build(),
                Seat.builder()
                        .row(2L)
                        .number(2L)
                        .build(),
                Seat.builder()
                        .row(2L)
                        .number(3L)
                        .build(),
                Seat.builder()
                        .row(2L)
                        .number(4L)
                        .build())
        );
        return seats;
    }
}
