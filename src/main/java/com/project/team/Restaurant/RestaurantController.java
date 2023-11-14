package com.project.team.Restaurant;

import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    private final SiteUserService siteUserService;


    @GetMapping("/register")
    public String register() {
        return "registerForm";
    }

    /*
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public String register(Principal principal) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            return "registerForm";
        }
        this.restaurantService.registerRestaurant(user);
        return "redirect:/map";
        }
     */
}
