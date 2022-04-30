package com.babior.ticketbookingapp.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Movie {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String genre;
    private int length;

    public Movie(String title) {
        this.title = title;
    }

    //    public Movie() {
//        this.id = -1;
//        this.title = "";
//        this.genre = "";
//        this.length = -1;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie blogEntry = (Movie) o;
        return title.equals(blogEntry.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
