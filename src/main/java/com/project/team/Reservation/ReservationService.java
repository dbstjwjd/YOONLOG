package com.project.team.Reservation;

import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public void reserveRestaurant(SiteUser customer, Restaurant restaurant, LocalDate date, LocalTime time, int count) {
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setRestaurant(restaurant);
        reservation.setDate(date);
        reservation.setTime(time);
        reservation.setCount(count);
        this.reservationRepository.save(reservation);
    }
}
