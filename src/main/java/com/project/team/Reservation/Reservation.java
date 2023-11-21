package com.project.team.Reservation;

import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class  Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;

    private LocalTime time;

    private LocalDateTime dateTime;

    private int count;

    @ManyToOne
    private SiteUser user;

    @ManyToOne
    private Restaurant restaurant;

    private String status;
}
