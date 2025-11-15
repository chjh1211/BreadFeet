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

    public BakeryListResponseDto(Bakery entity, double avgRating) {
        this.bakeryId = entity.getId();
        this.name = entity.getName();
        this.roadAddress = entity.getRoadAddress();
        this.city = entity.getCity();
        this.district = entity.getDistrict();
        this.avgRating = avgRating;
    }
}
