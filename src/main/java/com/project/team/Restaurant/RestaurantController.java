package com.project.team.Restaurant;

import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import com.project.team.Review.Review;
import com.project.team.Review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Random;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class
RestaurantController {

    private final RestaurantService restaurantService;

    private final SiteUserService siteUserService;

    private final ReviewService reviewService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/register")
    public String register(RestaurantRegisterForm restaurantRegisterForm) {
        return "registerForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public String register(Principal principal, @Valid RestaurantRegisterForm restaurantRegisterForm,
                           BindingResult bindingResult, Model model, @RequestPart(value = "image") MultipartFile image) throws IOException {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        if (bindingResult.hasErrors())
            return "registerForm";
        Restaurant restaurant = this.restaurantService.registerRestaurant(restaurantRegisterForm.getName(), restaurantRegisterForm.getAddress(),
                restaurantRegisterForm.getNumber(), restaurantRegisterForm.getFacilities(), restaurantRegisterForm.getMain(), user,
                restaurantRegisterForm.getStartTime(), restaurantRegisterForm.getEndTime(), restaurantRegisterForm.getIntroduce());
        this.restaurantService.uploadImage(restaurant, image);
        model.addAttribute("restaurant",restaurant);
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
                         @Valid RestaurantRegisterForm restaurantRegisterForm, BindingResult bindingResult, Model model,
                         @RequestPart(value = "image") MultipartFile image) throws IOException {
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        if (!restaurant.getOwner().getLoginId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        if (bindingResult.hasErrors())
            return "registerForm";
        model.addAttribute("restaurant", restaurant);
        this.restaurantService.modifyRestaurant(restaurantRegisterForm.getName(), restaurantRegisterForm.getAddress(),
                restaurantRegisterForm.getNumber(), restaurant, restaurantRegisterForm.getFacilities(), restaurantRegisterForm.getMain(),
                restaurantRegisterForm.getStartTime(), restaurantRegisterForm.getEndTime(), restaurantRegisterForm.getIntroduce());
        this.restaurantService.uploadImage(restaurant, image);
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
        return "redirect:/restaurant/page/" + restaurant.getOwner().getLoginId();

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
    public String detail(@PathVariable("id") Integer id, Model model, Principal principal) {
        if (principal != null) {
            SiteUser user = this.siteUserService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        List<Review> reviews = this.reviewService.getReviews(restaurant);
        double averageStar = this.reviewService.averageStar(reviews);
        model.addAttribute("averageStar", averageStar);
        model.addAttribute("reviews", reviews);
        model.addAttribute("restaurant", restaurant);
        return "restaurantDetail";
    }


    @PostMapping("/setLocation")
    public String setLocation(String x, String y, String restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurant(Integer.valueOf(restaurantId));
        restaurantService.setLocation(restaurant, x, y);
        return "redirect:/main";
    }

    //테스트용 코드
    @GetMapping("/test2")
    public String test2(Model model){
        List<Restaurant> restaurants = restaurantService.getAll();
        Random random = new Random(); //랜덤 함수
        int randomMenu = random.nextInt(restaurants.size()); // 랜덤한 메뉴인덱스 생성
        Restaurant restaurant = restaurants.get(randomMenu); // restaurant 객체에 조회한값 집어넣기
        model.addAttribute("restaurant", restaurant);
        return "test2";
    }
}


