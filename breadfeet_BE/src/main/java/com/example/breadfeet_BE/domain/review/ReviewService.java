package com.example.breadfeet_BE.domain.review;

import com.example.breadfeet_BE.auth.config.auth.dto.SessionUser;
import com.example.breadfeet_BE.domain.bakery.Bakery;
import com.example.breadfeet_BE.domain.bakery.BakeryRepository;
import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BakeryRepository bakeryRepository;


    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findReviewsByBakeryId(Long bakeryId){
        return reviewRepository.findAllWithUserByBakeryId(bakeryId)
                .stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createReview(ReviewCreateRequestDto requestDto, String userId, Long bakeyId){
        // 1. "누가" 썼는지, JWT에서 받은 userId로 User 엔티티를 조회
        //    (주의! JWT는 String을 주므로 Long으로 변환)
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. id=" + userId));

        // 2. "어느 빵집"에 썼는지, DTO에서 받은 bakeryId로 Bakery 엔티티를 조회
        Bakery bakery = bakeryRepository.findById(bakeyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 빵집을 찾을 수 없습니다. id=" + bakeyId));

        // 3. Review 엔티티 생성 (Builder 사용)
        Review newReview = Review.builder()
                .content(requestDto.getContent())
                .rating(requestDto.getRating())
                .user(user)       // ⬅️ 1번에서 찾은 User 연결
                .bakery(bakery)   // ⬅️ 2번에서 찾은 Bakery 연결
                .build();

        // 4. DB에 리뷰 저장
        Review savedReview = reviewRepository.save(newReview);

        // 5. 생성된 리뷰의 ID 반환
        return savedReview.getReviewid();
    }
}
