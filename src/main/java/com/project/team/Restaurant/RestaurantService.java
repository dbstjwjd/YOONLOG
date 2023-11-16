package com.project.team.Restaurant;

import com.project.team.DataNotFoundException;
import com.project.team.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Restaurant registerRestaurant(String name, String address, String number, List<String> facilities, String main, SiteUser owner) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setNumber(number);
        restaurant.setOwner(owner);
        restaurant.setFacilities(facilities);
        restaurant.setMain(main);
        restaurant.setRegDate(LocalDateTime.now());

        this.restaurantRepository.save(restaurant);
        return restaurant;
    }

    public Restaurant getRestaurant(Integer id) {
        Optional<Restaurant> or = this.restaurantRepository.findById(id);
        if (or.isPresent())
            return or.get();
        else throw new DataNotFoundException("restaurant not found");
    }

    public void modifyRestaurant(String name, String address, String number, Restaurant restaurant) {
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setNumber(number);
        this.restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(Restaurant restaurant) {
        this.restaurantRepository.delete(restaurant);
    }

    public void setLocation(Restaurant restaurant, String x, String y) {
        restaurant.setLocationX(x);
        restaurant.setLocationY(y);
        restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }
}

