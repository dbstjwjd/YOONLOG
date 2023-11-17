package com.project.team.Reservation;

import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;

    private LocalTime time;

    private int count;

    @ManyToOne
    private SiteUser customer;

    @ManyToOne
    private Restaurant restaurant;
}
