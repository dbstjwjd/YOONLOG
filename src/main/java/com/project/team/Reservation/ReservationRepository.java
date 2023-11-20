package com.project.team.Reservation;

import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Page<Reservation> findByUser(Pageable pageable, SiteUser user);

    List<Reservation> findByRestaurant(Restaurant restaurant);

    List<Reservation> findByDate(LocalDate date);

    List<Reservation> findByUser(SiteUser user);
}
