package com.project.team.Restaurant;

import com.project.team.DataNotFoundException;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import jakarta.validation.Valid;
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
    public String register(RestaurantRegisterForm restaurantRegisterForm) {
        return "registerForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public String register(Principal principal, @Valid RestaurantRegisterForm restaurantRegisterForm, BindingResult bindingResult) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        if (bindingResult.hasErrors())
            return "registerForm";
        this.restaurantService.registerRestaurant(restaurantRegisterForm.getName(), restaurantRegisterForm.getAddress(),
                restaurantRegisterForm.getNumber(), restaurantRegisterForm.getFacilities(), restaurantRegisterForm.getMain(), user,
                restaurantRegisterForm.getStartTime(), restaurantRegisterForm.getEndTime(), restaurantRegisterForm.getIntroduce());
        return "addressParser";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, Principal principal, RestaurantRegisterForm restaurantRegisterForm) {
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        if (!restaurant.getOwner().getLoginId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        restaurantRegisterForm.setName(restaurant.getName());
        restaurantRegisterForm.setAddress(restaurant.getAddress());
        restaurantRegisterForm.setNumber(restaurant.getNumber());
        restaurantRegisterForm.setFacilities(restaurant.getFacilities());
        restaurantRegisterForm.setStartTime(restaurant.getStartTime());
        restaurantRegisterForm.setEndTime(restaurant.getEndTime());
        restaurantRegisterForm.setIntroduce(restaurant.getIntroduce());
        restaurantRegisterForm.setMain(restaurant.getMain());
        return "registerForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, Principal principal,
                         @Valid RestaurantRegisterForm restaurantRegisterForm, BindingResult bindingResult) {
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        if (!restaurant.getOwner().getLoginId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        if (bindingResult.hasErrors())
            return "registerForm";
        this.restaurantService.modifyRestaurant(restaurantRegisterForm.getName(), restaurantRegisterForm.getAddress(),
                restaurantRegisterForm.getNumber(), restaurant, restaurantRegisterForm.getFacilities(), restaurantRegisterForm.getMain(),
                restaurantRegisterForm.getStartTime(), restaurantRegisterForm.getEndTime(), restaurantRegisterForm.getIntroduce());
        return "addressParser";
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


