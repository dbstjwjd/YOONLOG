package com.project.team;

import com.project.team.Restaurant.Restaurant;
import com.project.team.Restaurant.RestaurantService;
import com.project.team.User.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DataController {
    private final RestaurantService restaurantService;
    private final SiteUserService siteUserService;

    @GetMapping("/getData")
    public String getData() {
        return "getData";
    }

    @PostMapping(value = "/getData")
    public void receive(@RequestBody List<Map<String, String>> result) {
        boolean skip;
        for (Map<String, String> data : result) {
            skip = false;
            String address = data.get("road_address_name");
            String name = data.get("place_name");
            List<Restaurant> restaurants = restaurantService.getByAddress(address);
            for(Restaurant res : restaurants) {
                if(res.getName().equals(name)) skip = true;
            }
            if (skip) continue;
            Restaurant restaurant = restaurantService.registerRestaurant(
                    name,
                    address,
                    data.get("phone"),
                    null,
                    null,
                    null, LocalTime.MIN, LocalTime.MAX, null
            );
            restaurantService.setLocation(restaurant, data.get("x"), data.get("y"));
        }
    }

    @GetMapping("/interprocess")
    public String interprocess(@RequestParam(value = "inputAddress", defaultValue = "aroundMe") String inputAddress,
                                Model model) {
        model.addAttribute("inputAddress", inputAddress);
        return "interprocess";
    }
}
