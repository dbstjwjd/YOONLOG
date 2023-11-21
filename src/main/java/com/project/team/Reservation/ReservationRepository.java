package com.project.team.Reservation;

import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Page<Reservation> findByRestaurantIn(Pageable pageable, List<Restaurant> restaurants);

    List<Reservation> findByUser(SiteUser user);
}
