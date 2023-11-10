package com.ysblog.sbb.Post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysblog.sbb.Comment.Comment;
import com.ysblog.sbb.Comment.CommentService;
import com.ysblog.sbb.User.SiteUser;
import com.ysblog.sbb.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    private final UserService userService;

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(PostForm postForm, Model model, Principal principal, @RequestParam(value = "nickname", defaultValue = "") String nickname) {
        SiteUser user = this.userService.getUser(principal.getName());
        List<SiteUser> authorList = this.userService.searchUser(nickname);
        model.addAttribute("user", user);
        model.addAttribute("authorList", authorList);
        model.addAttribute("searchKw", nickname);
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid PostForm postForm, BindingResult bindingResult, Principal principal, Model model, @RequestParam(value = "inputTag", defaultValue = "") String hashtag) {
        SiteUser user = this.userService.getUser(principal.getName());
        model.addAttribute("user", user);
        if (bindingResult.hasErrors()) {
            return "post_form";
        }
        this.postService.createPost(postForm.getSubject(), postForm.getContent(), postForm.getCategory(), user, hashtag);
        return "redirect:/";
    }

    @GetMapping("/main")
    public String list(Model model, Principal principal, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw,
                       @RequestParam(value = "category", defaultValue = "") String category,
                       @RequestParam(value = "nickname", defaultValue = "") String nickname,
                       @RequestParam(value = "subUser", defaultValue = "") String username) {
        Page<Post> paging = this.postService.getList(page, kw, category);
        List<SiteUser> authorList = this.userService.searchUser(nickname);
        if (!username.isEmpty()) {
            List<Post> postList = this.postService.findByAuthor(this.userService.getUser(username));
            SiteUser user = this.userService.getUser(username);
            model.addAttribute("selectedUser", user);
            model.addAttribute("postList", postList);
        }
        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            Set<SiteUser> subscribed = this.userService.getSubscribed(user);
            model.addAttribute("subscribed", subscribed);
            model.addAttribute("user", user);
        }
        model.addAttribute("searchKw", nickname);
        model.addAttribute("authorList", authorList);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("category", category);
        return "post_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, Principal principal, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "nickname", defaultValue = "") String nickname) {
        Post post = this.postService.getPost(id);
        Page<Comment> paging = this.commentService.getList(post, page);
        List<SiteUser> authorList = this.userService.searchUser(nickname);
        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        List<String> tagList = getStrings(post);
        model.addAttribute("searchKw", nickname);
        model.addAttribute("authorList", authorList);
        model.addAttribute("tagList", tagList);
        model.addAttribute("paging", paging);
        model.addAttribute("post", post);
        return "post_detail";
    }

    private static List<String> getStrings(Post post) {
        String hashtag = post.getHashtag();
        if (hashtag == null || hashtag.isEmpty()) {
            return new ArrayList<>();
        }
        JSONParser parser = new JSONParser();
        List<String> tagList = new ArrayList<>();
        try {
            JSONArray jsonArray = (JSONArray) parser.parse(hashtag);
            for (Object item : jsonArray) {
                JSONObject jsonObject = (JSONObject) item;
                String value = (String) jsonObject.get("value");
                tagList.add(value);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return tagList;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(PostForm postForm, @PathVariable("id") Integer id, Principal principal, Model model, @RequestParam(value = "nickname", defaultValue = "") String nickname) {
        Post post = this.postService.getPost(id);
        SiteUser user = this.userService.getUser(principal.getName());
        List<SiteUser> authorList = this.userService.searchUser(nickname);
        model.addAttribute("user", user);
        model.addAttribute("authorList", authorList);
        model.addAttribute("searchKw", nickname);
        if (!post.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        postForm.setCategory(post.getCategory());
        postForm.setSubject(post.getSubject());
        postForm.setContent(post.getContent());
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, Principal principal, @Valid PostForm postForm, BindingResult bindingResult, @RequestParam(value = "inputTag", defaultValue = "") String hashtag) {
        Post post = this.postService.getPost(id);
        if (!post.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        if (bindingResult.hasErrors()) return "post_form";
        this.postService.modifyPost(post, postForm.getCategory(), postForm.getSubject(), postForm.getContent(), hashtag);
        return String.format("redirect:/post/detail/%s", id);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        Post post = this.postService.getPost(id);
        if (!post.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        this.postService.deletePost(post);
        return "redirect:/";
    }

    @GetMapping("/like/{id}")
    @PreAuthorize("isAuthenticated()")
    public String vote(@PathVariable("id") Integer id, Principal principal) {
        SiteUser user = this.userService.getUser(principal.getName());
        Post post = this.postService.getPost(id);
        this.postService.likePost(post, user);
        return String.format("redirect:/post/detail/%s", id);
    }

    @GetMapping("/search/{hashtag}")
    public String tagList(@PathVariable("hashtag") String hashtag, Model model, Principal principal, @RequestParam(value = "nickname", defaultValue = "") String nickname) {
        List<Post> postList = this.postService.searchTagList(hashtag);
        List<SiteUser> authorList = this.userService.searchUser(nickname);
        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        model.addAttribute("authorList", authorList);
        model.addAttribute("searchKw", nickname);
        model.addAttribute("postList", postList);
        return "hashtag_list";
    }
}
