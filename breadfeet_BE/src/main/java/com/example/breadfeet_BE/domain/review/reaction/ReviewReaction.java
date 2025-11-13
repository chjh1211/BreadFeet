package com.example.breadfeet_BE.domain.review.reaction;

import com.example.breadfeet_BE.domain.review.Review;
import com.example.breadfeet_BE.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review_reaction", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"review_id", "user_id"})
})
public class ReviewReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType reactionType;

    @Builder
    public ReviewReaction(Review review, User user, ReactionType reactionType) {
        this.review = review;
        this.user = user;
        this.reactionType = reactionType;
    }

    public void update(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}
