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

        // N+1 문제를 해결하기 위한 최적화
        List<Long> bakeryIds = bakeries.stream()
                .map(Bakery::getId)
                .toList();

        Map<Long, Double> avgRatingsMap = new HashMap<>();
        Map<Long, Integer> reviewCountsMap = new HashMap<>(); // 리뷰 개수 맵 추가

        if(!bakeryIds.isEmpty()){
            List<Object[]> avgRatings = reviewRepository.findAverageRatingsByBakeryIds(bakeryIds);
            avgRatingsMap = avgRatings.stream()
                    .collect(Collectors.toMap(
                            rating -> (Long) rating[0],
                            rating -> (Double) rating[1]
                    ));

            // 각 빵집의 리뷰 개수를 한 번에 조회 (N+1 방지)
            // 현재는 각 빵집별로 개별 쿼리 발생 (N+1 발생)
            for (Long bakeryId : bakeryIds) {
                reviewCountsMap.put(bakeryId, (int) reviewRepository.countByBakery_Id(bakeryId));
            }
        }


        Map<Long, Double> finalAvgRatingsMap = avgRatingsMap;
        Map<Long, Integer> finalReviewCountsMap = reviewCountsMap; // 람다에서 사용하기 위해 final 변수 선언
        Page<BakeryListResponseDto> dtoPage = bakeries.map(bakery -> {
            double finalRating = finalAvgRatingsMap.getOrDefault(bakery.getId(), 0.0);
            int finalReviewCount = finalReviewCountsMap.getOrDefault(bakery.getId(), 0); // 리뷰 개수 가져오기
            return new BakeryListResponseDto(bakery, finalRating, finalReviewCount); // reviewCount 전달
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

        int reviewCount = (int) reviewRepository.countByBakery_Id(bakeryId); // 리뷰 개수 조회

        return new BakeryDetailResponseDto(bakery, reviewCount); // reviewCount 전달
    }
}
