package com.project.team.Restaurant;

import com.project.team.Reservation.Reservation;
import com.project.team.User.SiteUser;
import com.project.team.review.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 20)
    private String name;

    private String address;

    private String number;

    @ManyToOne
    private SiteUser owner;

    private LocalDateTime regDate;

    private String main;


    private List<String> facilities;

    private String locationX;

    private String locationY;

    private String image;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations;

    @Column(columnDefinition = "TEXT")
    private String introduce;

    private LocalTime startTime;

    private LocalTime endTime;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews;
}
