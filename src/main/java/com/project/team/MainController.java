package com.project.team;

import com.project.team.Restaurant.RestaurantService;
import com.project.team.User.SiteUserService;
import com.project.team.User.UserCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final RestaurantService restaurantService;
    private final SiteUserService siteUserService;

    @GetMapping("/")
    public String index(UserCreateForm userCreateForm) {
        return "start";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "redirect:/map/view";
    }

    @GetMapping("/regTest")
    public String regTest() {
        return "registerForm";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
