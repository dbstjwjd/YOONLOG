package com.project.team.review;

import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Restaurant restaurant;
    @ManyToOne
    private SiteUser user;
    @Column(columnDefinition = "TEXT")
    private String comment;
    private Integer star;
    private LocalDateTime regDate;
}
