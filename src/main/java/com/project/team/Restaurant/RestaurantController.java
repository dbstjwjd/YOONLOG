package com.project.team.Restaurant;

import com.project.team.DataNotFoundException;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

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
                           @RequestParam(value = "address") String address, @RequestParam(value = "number") String number,
                           @RequestParam(value = "facilities", required = false) List<String> facilities, String main) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        this.restaurantService.registerRestaurant(name, address, number, facilities, main, user);
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

    @GetMapping("/restaurant/{id}")
    public String reserve(Model model,@PathVariable("id") Integer id, BindingResult bindingResult, Principal principal) {

        if (id == null) {
            throw new DataNotFoundException("음식점 ID가 필요합니다.");
        }

        Restaurant restaurant = restaurantService.getRestaurant(id);
        if (restaurant == null) {
            return "redirect:/form_errors";
        }

        String userId = principal.getName();
        SiteUser siteUser = siteUserService.getUser(userId);

        if (siteUser != null) {
            model.addAttribute("siteUser", siteUser);
        }

        return "reserve";


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
    public String detail(@PathVariable("id") Integer id) {
        return "restaurantDetail";
    }
}
