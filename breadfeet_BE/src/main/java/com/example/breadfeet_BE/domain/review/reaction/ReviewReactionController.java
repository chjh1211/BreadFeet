package com.example.breadfeet_BE.domain.review.reaction;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/{reviewId}")
public class ReviewReactionController {

    private final ReviewReactionService reviewReactionService;

    @PostMapping("/like")
    public ResponseEntity<Void> likeReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal String userId
    ) {
        reviewReactionService.addReaction(reviewId, userId, ReactionType.LIKE);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> dislikeReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal String userId
    ) {
        reviewReactionService.addReaction(reviewId, userId, ReactionType.DISLIKE);
        return ResponseEntity.ok().build();
    }
}
