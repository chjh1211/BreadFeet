package com.example.breadfeet_BE.domain.bakery;

import com.example.breadfeet_BE.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BakeryService {
    private final BakeryRepository bakeryRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public BakeryPageResponseDto findAllBakeries(String searchQuery, Pageable pageable){
        Page<Bakery> bakeries;

        if (searchQuery == null || searchQuery.isBlank()){
            bakeries = bakeryRepository.findAll(pageable);
        }
        else{
            bakeries = bakeryRepository.findByNameContaining(searchQuery, pageable);
        }

        Page<BakeryListResponseDto> dtoPage = bakeries.map(BakeryListResponseDto::new);
        return new BakeryPageResponseDto(dtoPage);
    }

    @Transactional(readOnly = true)
    public BakeryDetailResponseDto findBakeryById(Long bakeryId){
        Bakery bakery = bakeryRepository.findById(bakeryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 빵집을 찾을 수 없습니다. id=" + bakeryId));
        Double avgRating = reviewRepository.findAverageRatingByBakeryId(bakeryId);
        double finalRating = (avgRating == null) ? 0.0 : avgRating;
        bakery.setAvgRating(finalRating);

        return new BakeryDetailResponseDto(bakery);
    }
}
