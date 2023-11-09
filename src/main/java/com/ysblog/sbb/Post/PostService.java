package com.ysblog.sbb.Post;

import com.ysblog.sbb.Comment.Comment;
import com.ysblog.sbb.DataNotFoundException;
import com.ysblog.sbb.User.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private Specification<Post> search(String kw, String category) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Post> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Post, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Post, Comment> a = q.join("commentList", JoinType.LEFT);
                Join<Comment, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.and(
                        cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                                cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                                cb.like(u1.get("nickname"), "%" + kw + "%"),    // 질문 작성자
                                cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                                cb.like(u2.get("nickname"), "%" + kw + "%")),    // 답변 작성자
                        cb.like(q.get("category"), "%" + category + "%") // 카테고리
                );
            }
        };
    }

    public void createPost(String subject, String content, String category, SiteUser author, String hashtag) {
        Post post = new Post();
        post.setSubject(subject);
        post.setContent(content);
        post.setCategory(category);
        post.setAuthor(author);
        post.setHashtag(hashtag);
        post.setCreateDate(LocalDateTime.now());
        this.postRepository.save(post);
    }

    public Page<Post> getList(int page, String kw, String category) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Post> spec = search(kw, category);
        return this.postRepository.findAll(spec, pageable);
    }

    public Post getPost(Integer id) {
        Optional<Post> op = this.postRepository.findById(id);
        if (op.isPresent()) {
            Post post = op.get();
            post.setView(post.getView() + 1);
            this.postRepository.save(post);
            return post;
        } else {
            throw new DataNotFoundException("post not found");
        }
    }

    public void modifyPost(Post post, String category, String subject, String content, String hashtag) {
        post.setCategory(category);
        post.setSubject(subject);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());
        post.setHashtag(hashtag);
        this.postRepository.save(post);
    }

    public void deletePost(Post post) {
        this.postRepository.delete(post);
    }

    public void likePost(Post post, SiteUser liker) {
        post.getLiker().add(liker);
        this.postRepository.save(post);
    }

    public List<Post> searchTagList(String hashtag) {
        return this.postRepository.findByHashtagLike("%" + hashtag + "%");
    }

}
