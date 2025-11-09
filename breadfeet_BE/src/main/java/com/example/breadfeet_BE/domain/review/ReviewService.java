package com.example.breadfeet_BE.domain.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findReviewsByBakeryId(Long bakeryId){
        return reviewRepository.findAllByBakeryId(bakeryId)
                .stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

}
