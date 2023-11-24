package com.project.team.Restaurant;

import com.project.team.DataNotFoundException;
import com.project.team.User.SiteUser;
import com.project.team.review.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private String uploadPath = "C:/uploads/restaurant";

    public Restaurant registerRestaurant(String name, String address, String number, List<String> facilities, String main,
                                         SiteUser owner, LocalTime startTime, LocalTime endTime, String introduce) throws IOException {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setNumber(number);
        restaurant.setOwner(owner);
        restaurant.setFacilities(facilities);
        restaurant.setMain(main);
        restaurant.setRegDate(LocalDateTime.now());
        restaurant.setStartTime(startTime);
        restaurant.setEndTime(endTime);
        restaurant.setIntroduce(introduce);
        this.restaurantRepository.save(restaurant);
        return restaurant;
    }

    public Restaurant getRestaurant(Integer id) {
        Optional<Restaurant> or = this.restaurantRepository.findById(id);
        if (or.isPresent())
            return or.get();
        else throw new DataNotFoundException("restaurant not found");
    }

    public void modifyRestaurant(String name, String address, String number, Restaurant restaurant, List<String> facilities,
                                 String main, LocalTime startTime, LocalTime endTime, String introduce) {
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setNumber(number);
        restaurant.setFacilities(facilities);
        restaurant.setMain(main);
        restaurant.setStartTime(startTime);
        restaurant.setEndTime(endTime);
        restaurant.setIntroduce(introduce);
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

    public List<Restaurant> getByAddress(String address) {
        return restaurantRepository.findByAddress(address);
    }

    public List<Restaurant> getAround(String x, String y, double radius) {
        double myX = Double.parseDouble(x);
        double myY = Double.parseDouble(y);
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        List<Restaurant> restaurants = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            double resX = Double.parseDouble(restaurant.getLocationX());
            double resY = Double.parseDouble(restaurant.getLocationY());
            if (resX < myX + radius && resX > myX - radius && resY < myY + radius && resY > myY - radius)
                restaurants.add(restaurant);
        }
        return restaurants;
    }

    public void uploadImage(Restaurant restaurant, MultipartFile image) throws IOException {
        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        if (image.isEmpty())
            restaurant.setImage("/image/startImage.jpg");
        else {
            String fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
            String fileName = restaurant.getId() + "." + fileExtension;
            File dest = new File(uploadPath + File.separator + fileName);
            FileCopyUtils.copy(image.getBytes(), dest);
            restaurant.setImage("/restaurant/image/" + restaurant.getId() + "." + fileExtension);
        }
        this.restaurantRepository.save(restaurant);
    }

    public void setImage(Restaurant restaurant, String image) {
        restaurant.setImage(image);
        restaurantRepository.save(restaurant);
    }
}

