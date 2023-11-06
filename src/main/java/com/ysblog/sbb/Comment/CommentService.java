package com.ysblog.sbb.Comment;

import com.ysblog.sbb.DataNotFoundException;
import com.ysblog.sbb.Post.Post;
import com.ysblog.sbb.User.SiteUser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Page<Comment> getList(Post post, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 2, Sort.by(sorts));
        return this.commentRepository.findAllByPost(post, pageable);
    }

    public void createComment(String content, Post post, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setCreateDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    public void modifyComment(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    public Comment getComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else throw new DataNotFoundException("comment not found");
    }

    public void deleteComment(Comment comment) {
        this.commentRepository.delete(comment);
    }

    public void likeComment(Comment comment, SiteUser liker) {
        comment.getLiker().add(liker);
        this.commentRepository.save(comment);
    }
}
