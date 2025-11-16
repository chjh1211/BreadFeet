package com.example.breadfeet_BE.domain.bakery;

import com.example.breadfeet_BE.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        List<Long> bakeryIds = bakeries.stream()
                .map(Bakery::getId)
                .toList();

        Map<Long, Double> avgRatingsMap = new HashMap<>();
        Map<Long, Long> reviewCountsMap = new HashMap<>(); // 리뷰 개수 맵 (Long으로 변경)

        if(!bakeryIds.isEmpty()){
            List<Object[]> avgRatings = reviewRepository.findAverageRatingsByBakeryIds(bakeryIds);
            avgRatingsMap = avgRatings.stream()
                    .collect(Collectors.toMap(
                            rating -> (Long) rating[0],
                            rating -> (Double) rating[1]
                    ));

            List<Object[]> reviewCounts = reviewRepository.findReviewCountsByBakeryIds(bakeryIds);
            reviewCountsMap = reviewCounts.stream()
                    .collect(Collectors.toMap(
                            count -> (Long) count[0],
                            count -> (Long) count[1]
                    ));
        }

        Map<Long, Double> finalAvgRatingsMap = avgRatingsMap;
        Map<Long, Long> finalReviewCountsMap = reviewCountsMap;
        Page<BakeryListResponseDto> dtoPage = bakeries.map(bakery -> {
            double finalRating = finalAvgRatingsMap.getOrDefault(bakery.getId(), 0.0);
            int finalReviewCount = finalReviewCountsMap.getOrDefault(bakery.getId(), 0L).intValue();
            return new BakeryListResponseDto(bakery, finalRating, finalReviewCount);
        });
        return new BakeryPageResponseDto(dtoPage);
    }

    @Transactional(readOnly = true)
    public BakeryDetailResponseDto findBakeryById(Long bakeryId){
        Bakery bakery = bakeryRepository.findById(bakeryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 빵집을 찾을 수 없습니다. id=" + bakeryId));
        Double avgRating = reviewRepository.findAverageRatingByBakeryId(bakeryId);
        double finalRating = (avgRating == null) ? 0.0 : avgRating;
        bakery.setAvgRating(finalRating);

        int reviewCount = (int) reviewRepository.countByBakery_Id(bakeryId);

        return new BakeryDetailResponseDto(bakery, reviewCount);
    }
}
