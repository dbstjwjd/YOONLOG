package com.project.team.Restaurant;

import com.project.team.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public void registerRestaurant(String name, String address, String number, SiteUser owner) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setNumber(number);
        restaurant.setOwner(owner);
        this.restaurantRepository.save(restaurant);
    }
}
