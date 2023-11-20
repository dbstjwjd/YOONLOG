package com.project.team.Reservation;

import com.project.team.User.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Page<Reservation> findByUser(Pageable pageable, SiteUser user);
}
