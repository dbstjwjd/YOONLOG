package com.project.team.Restaurant;

import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    private final SiteUserService siteUserService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/register")
    public String register() {
        return "registerForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public String register(Principal principal, @RequestParam(value = "name") String name,
                           @RequestParam(value = "address") String address, @RequestParam(value = "number") String number) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        this.restaurantService.registerRestaurant(name, address, number, user);
        return "redirect:/main";
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
                         @RequestParam(value = "address") String address, @RequestParam(value = "number") String number) {
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        if (!restaurant.getOwner().getLoginId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        this.restaurantService.modifyRestaurant(name, address, number, restaurant);
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
}
