package com.project.team.review;

import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

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

    private LocalDateTime modifyDate;

    private String image1;

    private String image2;

    private String image3;

}
