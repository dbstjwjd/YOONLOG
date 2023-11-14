package com.project.team.User;

import com.project.team.Restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    private String createDate;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Restaurant> restaurants;
}