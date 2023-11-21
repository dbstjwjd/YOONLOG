package com.project.team.Reservation;

import com.project.team.DataNotFoundException;
import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public void reserveRestaurant(SiteUser user, Restaurant restaurant, LocalDate date, LocalTime time, int count) {
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRestaurant(restaurant);
        reservation.setDate(date);
        reservation.setTime(time);
        reservation.setCount(count);
        this.reservationRepository.save(reservation);
    }

    public Page<Reservation> getAllReservation(int page, List<Restaurant> restaurants) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("date"));
        sorts.add(Sort.Order.asc("time"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.reservationRepository.findByRestaurantIn(pageable, restaurants);
    }

    public Reservation getReservation(Integer id) {
        Optional<Reservation> reservation = this.reservationRepository.findById(id);
        if (reservation.isPresent()) {
            return reservation.get();
        } else throw new DataNotFoundException("reservation not found");
    }

    public void approveReservation(String status, Reservation reservation) {
        reservation.setStatus(status);
        this.reservationRepository.save(reservation);
    }

    public void refuseReservation(Reservation reservation) {
        this.reservationRepository.delete(reservation);
    }

    public List<Reservation> getAllByUser(SiteUser user) {
        return this.reservationRepository.findByUser(user);
    }
}
