package com.babior.ticketbookingapp.business.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @GeneratedValue
    private Long id;
    private String title;
    private int runningTime;

    public Movie(String title, int runningTime) {
        this.title = title;
        this.runningTime = runningTime;
    }
}
