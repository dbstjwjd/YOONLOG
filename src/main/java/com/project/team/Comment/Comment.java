package com.project.team.Comment;

import com.project.team.Review.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToOne
    private Review review;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

}
