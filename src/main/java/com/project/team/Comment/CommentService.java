package com.project.team.Comment;

import com.project.team.DataNotFoundException;
import com.project.team.Review.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void modifyComment(Comment comment, String content) {
        comment.setContent(content);
        this.commentRepository.save(comment);
    }

    public Comment getComment(Integer id) {
        Optional<Comment> oc = this.commentRepository.findById(id);
        if (oc.isPresent())
            return oc.get();
        else throw new DataNotFoundException("comment not found");
    }

    public void deleteComment(Comment comment) {
        this.commentRepository.delete(comment);
    }
}
