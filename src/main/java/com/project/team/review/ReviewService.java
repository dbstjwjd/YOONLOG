package com.project.team.review;

import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public void create(Restaurant restaurant, SiteUser user, Integer star, String comment) {
        Review review = new Review();
        review.setRestaurant(restaurant);
        review.setUser(user);
        review.setStar(star);
        review.setComment(comment);
        review.setRegDate(LocalDateTime.now());
        reviewRepository.save(review);
    }

    public void createTmp(Restaurant restaurant, String url) {
        Review review = new Review();
        review.setRestaurant(restaurant);
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("list_evaluation");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
