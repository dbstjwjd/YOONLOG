package com.project.team.Comment;

import com.project.team.Review.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public void createComment(Review review, String content) {
        Comment comment = new Comment();
        comment.setReview(review);
        comment.setContent(content);
        this.commentRepository.save(comment);
    }
}
