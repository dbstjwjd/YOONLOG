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
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    private final SiteUserService siteUserService;

    private final RestaurantService restaurantService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String create(String comment, Integer star, @PathVariable("id") Integer id, Principal principal) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        Restaurant restaurant = this.restaurantService.getRestaurant(id);
        this.reviewService.createReview(restaurant, user, star, comment);
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
    public String modify(@PathVariable("id") Integer id, Integer star, String comment, Principal principal) {
        Review review = this.reviewService.getReview(id);
        if (!review.getUser().getLoginId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        this.reviewService.modifyReview(review, star, comment);
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
