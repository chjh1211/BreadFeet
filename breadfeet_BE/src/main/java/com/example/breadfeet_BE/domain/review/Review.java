package com.example.breadfeet_BE.domain.review;

import com.example.breadfeet_BE.domain.bakery.Bakery;
import com.example.breadfeet_BE.domain.base.BaseEntity;
import com.example.breadfeet_BE.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewid;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bakery_id")
    private Bakery bakery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Review(String content, double rating, User user, Bakery bakery) {
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.bakery = bakery;
    }

    public void update(String content, double rating) {
        this.content = content;
        this.rating = rating;
    }
}
