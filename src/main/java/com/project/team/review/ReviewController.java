package com.project.team.review;

import com.project.team.Restaurant.Restaurant;
import com.project.team.Restaurant.RestaurantService;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    private final SiteUserService siteUserService;

    private final RestaurantService restaurantService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String create(String comment, Integer star, @PathVariable("id") Integer id, Principal principal,
                         @RequestParam("image1") MultipartFile image1, @RequestParam("image2") MultipartFile image2,
                         @RequestParam("image3") MultipartFile image3) throws IOException {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        Review review = this.reviewService.createReview(restaurant, user, star, comment);
        restaurant.setAverageStar(this.reviewService.averageStar(restaurant.getReviews()));
        this.reviewService.uploadImage(review, image1, image2, image3);
        return String.format("redirect:/restaurant/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, Principal principal, Model model) {
        Review review = this.reviewService.getReview(id);
        model.addAttribute("review", review);
        if (!review.getUser().getLoginId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        return "reviewModify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, Integer star, String comment, Principal principal,
                         @RequestParam("image1") MultipartFile image1, @RequestParam("image2") MultipartFile image2,
                         @RequestParam("image3") MultipartFile image3) throws IOException {
        Review review = this.reviewService.getReview(id);
        if (!review.getUser().getLoginId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        this.reviewService.modifyReview(review, star, comment);
        this.reviewService.uploadImage(review, image1, image2, image3);
        Restaurant restaurant = review.getRestaurant();
        restaurant.setAverageStar(this.reviewService.averageStar(restaurant.getReviews()));
        return String.format("redirect:/restaurant/detail/%s", review.getRestaurant().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        Review review = this.reviewService.getReview(id);
        if (!review.getUser().getLoginId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        this.reviewService.deleteReview(review);
        return String.format("redirect:/restaurant/detail/%s", review.getRestaurant().getId());
    }
}
