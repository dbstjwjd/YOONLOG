package com.project.team.User;

import com.project.team.Reservation.Reservation;
import com.project.team.Restaurant.Restaurant;
import com.project.team.review.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter

public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String loginId;

    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    private LocalDateTime createDate;

    private String authority;

//    ========================================
    @Column(unique = true)
    private String token;


    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Restaurant> restaurants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    private String picture;
    private String role = "ROLE_USER";

    public SiteUser(){

    }

    public SiteUser(String loginId, String name, String email, String picture,LocalDateTime createDate) {
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.createDate = createDate;
    }

    public SiteUser update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }
}
