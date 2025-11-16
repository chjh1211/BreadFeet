package com.example.breadfeet_BE.domain.bakery;

import lombok.Getter;

@Getter
public class BakeryListResponseDto {
    private Long bakeryId;
    private String name;
    private String city;
    private String district;
    private String roadAddress;
    private double avgRating;
    private int reviewCount; // 리뷰 개수 추가

    public BakeryListResponseDto(Bakery entity, double avgRating, int reviewCount) {
        this.bakeryId = entity.getId();
        this.name = entity.getName();
        this.roadAddress = entity.getRoadAddress();
        this.city = entity.getCity();
        this.district = entity.getDistrict();
        this.avgRating = avgRating;
        this.reviewCount = reviewCount;
    }
}
