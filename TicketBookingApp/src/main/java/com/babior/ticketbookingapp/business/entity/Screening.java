package com.babior.ticketbookingapp.business.entity;

import com.babior.ticketbookingapp.business.view.ScreeningView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Screening {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JsonView(ScreeningView.ResponseView.class)
    private Movie movie;
    @ManyToOne
    private Room room;
    @JsonView(ScreeningView.ResponseView.class)
    private LocalDateTime startDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Screening screening = (Screening) o;
        return id != null && Objects.equals(id, screening.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
