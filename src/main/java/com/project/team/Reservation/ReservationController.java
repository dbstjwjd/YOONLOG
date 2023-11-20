package com.project.team.Reservation;

import com.project.team.Restaurant.Restaurant;
import com.project.team.Restaurant.RestaurantService;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReservationController {

    private final ReservationService reservationService;

    private final RestaurantService restaurantService;

    private final SiteUserService siteUserService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}")
    public String reserve(@PathVariable("id") Integer id, Principal principal, @RequestParam("date") LocalDate date,
                          @RequestParam("time") LocalTime time, @RequestParam("count") int count) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        this.reservationService.reserveRestaurant(user, restaurant, date, time, count);
        return String.format("redirect:/restaurant/detail/%s", id);
    }

    // 사장 -> 예약 관리
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/manage")
    public String manage(Principal principal, Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        Page<Reservation> paging = this.reservationService.getAllReservation(page, user);
        model.addAttribute("paging", paging);
        model.addAttribute("user", user);
        return "reserveManagement";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/status/{id}")
    public String status(@PathVariable("id") Integer id, String status) {
        Reservation reservation = this.reservationService.getReservation(id);
        if (status.equals("승인 ")) {
            this.reservationService.approveReservation("예약 확정", reservation);
        } else
            this.reservationService.refuseReservation(reservation);
        return String.format("redirect:/reserve/manage");
    }

    // 손님 -> 예약 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/check")
    public String check(Principal principal, Model model) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        model.addAttribute("user", user);
        return "reserveCheck";
    }
}