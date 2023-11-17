package com.project.team.Restaurant;

import com.project.team.User.SiteUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
}
