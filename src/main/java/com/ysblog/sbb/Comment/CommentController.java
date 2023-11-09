package com.ysblog.sbb.Comment;

import com.ysblog.sbb.Post.Post;
import com.ysblog.sbb.Post.PostService;
import com.ysblog.sbb.User.SiteUser;
import com.ysblog.sbb.User.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    private final PostService postService;

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String create(@PathVariable("id") Integer id, String content, Principal principal) {
        Post post = this.postService.getPost(id);
        SiteUser user = this.userService.getUser(principal.getName());
        this.commentService.createComment(content, post, user);
        return String.format("redirect:/post/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(Model model, @PathVariable("id") Integer id, Principal principal,
                         @RequestParam(value = "nickname", defaultValue = "") String nickname) {
        SiteUser user = this.userService.getUser(principal.getName());
        Comment comment = this.commentService.getComment(id);
        List<SiteUser> authorList = this.userService.searchUser(nickname);
        model.addAttribute("authorList", authorList);
        model.addAttribute("user", user);
        model.addAttribute("comment", comment);
        model.addAttribute("searchKw", nickname);
        if (!comment.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, String content, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        this.commentService.modifyComment(comment, content);
        return String.format("redirect:/post/detail/%s", comment.getPost().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.commentService.deleteComment(comment);
        return String.format("redirect:/post/detail/%s", comment.getPost().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String like(@PathVariable("id") Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        SiteUser user = this.userService.getUser(principal.getName());
        this.commentService.likeComment(comment, user);
        return String.format("redirect:/post/detail/%s", comment.getPost().getId());
    }
}
