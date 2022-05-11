package com.babior.ticketbookingapp.business.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"title", "runningTime"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Movie {
    @Id
    @GeneratedValue(generator = "movie_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "movie_sequence", sequenceName = "movie_sequence", allocationSize = 1)
    private Long id;
    private String title;
    private int runningTime;

    public Movie(String title, int runningTime) {
        this.title = title;
        this.runningTime = runningTime;
    }
}
