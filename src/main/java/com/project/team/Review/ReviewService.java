package com.project.team.Review;

import com.project.team.DataNotFoundException;
import com.project.team.Restaurant.Restaurant;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final SiteUserService siteUserService;
    private final String HEAD = "https://place.map.kakao.com/main/v";

    private String uploadPath = "C:/uploads/review";

    public Review createReview(Restaurant restaurant, SiteUser user, Integer star, String comment) {
        Review review = new Review();
        review.setRestaurant(restaurant);
        review.setUser(user);
        review.setStar(star);
        review.setComment(comment);
        review.setRegDate(LocalDateTime.now());
        reviewRepository.save(review);
        return review;
    }

    public void createTmp(Restaurant restaurant, JSONArray comments) {
        for (Object o : comments) {
            JSONObject comment = (JSONObject) o;
            SiteUser user = null;
            String contents = null;
            String username = null;
            try {
                contents = comment.get("contents").toString();
                username = comment.get("username").toString();
            } catch (Exception ignored) {
            }
            if (username != null)
                user = siteUserService.getTestUser();
            Review review = new Review();
            review.setRestaurant(restaurant);
            review.setUser(user);
            review.setComment(contents);
            review.setStar((Integer.valueOf(comment.get("point").toString())));
            reviewRepository.save(review);
        }
    }


    public List<Review> getReviews(Restaurant restaurant) {
        return this.reviewRepository.findByRestaurant(restaurant);
    }

    public Review getReview(Integer id) {
        Optional<Review> review = this.reviewRepository.findById(id);
        if (review.isPresent()) return review.get();
        else throw new DataNotFoundException("review not found");
    }

    public double averageStar(List<Review> reviews) {
        if (reviews.isEmpty()) return 0;
        int totalStar = reviews.stream().mapToInt(Review::getStar).sum();
        return (double) totalStar / reviews.size();
    }

    public void deleteReview(Review review) {
        this.reviewRepository.delete(review);
    }

    public void modifyReview(Review review, Integer star, String comment) {
        review.setStar(star);
        review.setComment(comment);
        review.setModifyDate(LocalDateTime.now());
        this.reviewRepository.save(review);
    }

    public void uploadImage(Review review, MultipartFile image1, MultipartFile image2, MultipartFile image3) throws IOException {
        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        if (!image1.isEmpty()) {
            String fileExtension = StringUtils.getFilenameExtension(image1.getOriginalFilename());
            String fileName = review.getId() + "_1." + fileExtension;
            File dest = new File(uploadPath + File.separator + fileName);
            FileCopyUtils.copy(image1.getBytes(), dest);
            review.setImage1("/review/image/" + review.getId() + "_1." + fileExtension);
        }
        if (!image2.isEmpty()) {
            String fileExtension = StringUtils.getFilenameExtension(image2.getOriginalFilename());
            String fileName = review.getId() + "_2." + fileExtension;
            File dest = new File(uploadPath + File.separator + fileName);
            FileCopyUtils.copy(image2.getBytes(), dest);
            review.setImage2("/review/image/" + review.getId() + "_2." + fileExtension);
        }
        if (!image3.isEmpty()) {
            String fileExtension = StringUtils.getFilenameExtension(image3.getOriginalFilename());
            String fileName = review.getId() + "_3." + fileExtension;
            File dest = new File(uploadPath + File.separator + fileName);
            FileCopyUtils.copy(image3.getBytes(), dest);
            review.setImage3("/review/image/" + review.getId() + "_3." + fileExtension);
        }
        this.reviewRepository.save(review);
    }
}
