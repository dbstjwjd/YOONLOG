package com.project.team.Restaurant;

import com.project.team.DataNotFoundException;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class
RestaurantController {

    private final RestaurantService restaurantService;

    private final SiteUserService siteUserService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/register")
    public String register() {
        return "registerForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public String register(Principal principal, @RequestParam(value = "name") String name, Model model,
                           @RequestParam(value = "address") String address, @RequestParam(value = "number") String number,
                           @RequestParam(value = "facilities", required = false) List<String> facilities, String main,
                           LocalTime startTime, LocalTime endTime, String introduce) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        Restaurant restaurant = this.restaurantService.registerRestaurant(name, address, number, facilities, main, user, startTime, endTime, introduce);
        model.addAttribute("restaurant", restaurant);
        return "addressParser";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, Principal principal, Model model) {
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        model.addAttribute("restaurant", restaurant);
        if (!restaurant.getOwner().getLoginId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        return "registerForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, Principal principal, @RequestParam(value = "name") String name,
                         @RequestParam(value = "address") String address, @RequestParam(value = "number") String number,
                         @RequestParam(value = "facilities", required = false) List<String> facilities, String main,
                         LocalTime startTime, LocalTime endTime, String introduce) {
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        if (!restaurant.getOwner().getLoginId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        this.restaurantService.modifyRestaurant(name, address, number, restaurant, facilities, main, startTime, endTime, introduce);
        return "redirect:/main";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        if (!restaurant.getOwner().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.restaurantService.deleteRestaurant(restaurant);
        return "redirect:/main";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/page/{id}")
    public String management(@PathVariable("id") String loginId, Model model) {
        SiteUser user = this.siteUserService.getUser(loginId);
        List<Restaurant> restaurants = user.getRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "management";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        model.addAttribute("restaurant", restaurant);
        return "restaurantDetail";
    }

    @PostMapping("/setLocation")
    public String setLocation(String x, String y, String restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurant(Integer.valueOf(restaurantId));
        restaurantService.setLocation(restaurant, x, y);
        return "redirect:/main";
    }
}
