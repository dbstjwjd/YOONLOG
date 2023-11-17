package com.project.team.Reservation;

import com.project.team.Restaurant.Restaurant;
import com.project.team.Restaurant.RestaurantService;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    private final RestaurantService restaurantService;

    private final SiteUserService siteUserService;

    @PostMapping("/reserve/{id}")
    public String reserve(@PathVariable("id") Integer id, Principal principal, @RequestParam("date") LocalDate date,
                          @RequestParam("time")LocalTime time, @RequestParam("count") int count) {
        SiteUser customer = this.siteUserService.getUser(principal.getName());
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        this.reservationService.reserveRestaurant(customer, restaurant, date, time, count);
        return String.format("redirect:/restaurant/detail/%s", id);
    }
}
