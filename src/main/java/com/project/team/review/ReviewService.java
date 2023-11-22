package com.project.team.review;

import com.project.team.DataNotFoundException;
import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final SiteUserService siteUserService;
    private final String HEAD = "https://place.map.kakao.com/main/v";

    public void createReview(Restaurant restaurant, SiteUser user, Integer star, String comment) {
        Review review = new Review();
        review.setRestaurant(restaurant);
        review.setUser(user);
        review.setStar(star);
        review.setComment(comment);
        review.setRegDate(LocalDateTime.now());
        reviewRepository.save(review);
    }

    public void createTmp(Restaurant restaurant, String url) {
        try {
            String data = Jsoup.connect(this.HEAD + url.substring(url.lastIndexOf("/"))).ignoreContentType(true).execute().body();
            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(data);
            if (json.get("comment") == null) return;
            JSONArray comments = (JSONArray) ((JSONObject) json.get("comment")).get("list");
            for (Object o : comments) {
                JSONObject comment = (JSONObject) o;
                SiteUser user = siteUserService.create(null, "temp", comment.get("username").toString(), null, "손님");
                Review review = new Review();
                review.setRestaurant(restaurant);
                review.setUser(user);
                review.setComment(comment.get("contents").toString());
                review.setStar((Integer.valueOf(comment.get("point").toString())));
                reviewRepository.save(review);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Review> getReviews(Restaurant restaurant) {
        return this.reviewRepository.findByRestaurant(restaurant);
    }

    public Review getReview(Integer id) {
        Optional<Review> review = this.reviewRepository.findById(id);
        if (review.isPresent())
            return review.get();
        else throw new DataNotFoundException("review not found");
    }

    public double averageStar(List<Review> reviews) {
        if (reviews.isEmpty())
            return 0;
        int totalStar = reviews.stream().mapToInt(Review::getStar).sum();
        return (double) totalStar / reviews.size();
    }

    public void deleteReview(Review review) {
        this.reviewRepository.delete(review);
    }

}
