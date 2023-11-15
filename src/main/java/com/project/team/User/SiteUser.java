package com.project.team.User;

import com.project.team.Restaurant.Restaurant;
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
    private Long id;

    @Column(unique = true)
    private String loginId;

    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    private LocalDateTime createDate;

    private String authority;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Restaurant> restaurants;
}
