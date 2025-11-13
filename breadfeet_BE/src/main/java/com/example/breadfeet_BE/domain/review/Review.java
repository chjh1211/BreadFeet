package com.example.breadfeet_BE.domain.review;

import com.example.breadfeet_BE.domain.bakery.Bakery;
import com.example.breadfeet_BE.domain.base.BaseEntity;
import com.example.breadfeet_BE.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import com.example.breadfeet_BE.domain.review.reaction.ReviewReaction;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private double rating;

    @Formula("(select count(1) from review_reaction rr where rr.review_id = review_id and rr.reaction_type = 'LIKE')")
    private int likesCount;

    @Formula("(select count(1) from review_reaction rr where rr.review_id = review_id and rr.reaction_type = 'DISLIKE')")
    private int dislikesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bakery_id")
    private Bakery bakery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewReaction> reactions = new ArrayList<>();

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
