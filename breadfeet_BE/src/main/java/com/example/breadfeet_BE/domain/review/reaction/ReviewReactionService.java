package com.example.breadfeet_BE.domain.review.reaction;

import com.example.breadfeet_BE.domain.review.Review;
import com.example.breadfeet_BE.domain.review.ReviewRepository;
import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewReactionService {

    private final ReviewReactionRepository reviewReactionRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addReaction(Long reviewId, String userId, ReactionType reactionType) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다. id=" + reviewId));

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. id=" + userId));

        Optional<ReviewReaction> existingReaction = reviewReactionRepository.findByReviewIdAndUserId(reviewId, Long.parseLong(userId));

        if (existingReaction.isPresent()) {
            ReviewReaction reaction = existingReaction.get();
            if (reaction.getReactionType() == reactionType) {
                // 같은 반응을 다시 누르면 취소 (삭제)
                reviewReactionRepository.delete(reaction);
            } else {
                // 다른 반응을 누르면 변경 (업데이트)
                reaction.update(reactionType);
            }
        } else {
            // 처음 반응을 누르면 생성
            ReviewReaction newReaction = ReviewReaction.builder()
                    .review(review)
                    .user(user)
                    .reactionType(reactionType)
                    .build();
            reviewReactionRepository.save(newReaction);
        }
    }
}
