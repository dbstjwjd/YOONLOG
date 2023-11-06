package com.ysblog.sbb;

import com.ysblog.sbb.Comment.Comment;
import com.ysblog.sbb.Comment.CommentService;
import com.ysblog.sbb.Post.Post;
import com.ysblog.sbb.Post.PostService;
import com.ysblog.sbb.User.SiteUser;
import com.ysblog.sbb.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.time.LocalDateTime;

@SpringBootTest
class SbbApplicationTests {
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {

        SiteUser user1 = this.userService.getUser("aaa");
        SiteUser user2 = this.userService.getUser("bbb");
        SiteUser user3 = this.userService.getUser("ccc");


        /*
        for (int i = 1; i <= 30; i++) {
            this.postService.createPost(String.format("테스트용 게시물 %d", i), "내용없음", "일기장", user1);
            this.postService.createPost(String.format("테스트용 게시물 %d", i), "내용없음", "개발 일지", user2);
            this.postService.createPost(String.format("테스트용 게시물 %d", i), "내용없음", "잡담", user3);
        }

         */
    }
}


