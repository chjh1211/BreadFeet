package com.example.breadfeet_BE.domain.bakery;

import lombok.Getter;

@Getter
public class BakeryDetailResponseDto {
    private Long bakeryId;
    private String name;
    private String bestBread;
    private String phoneNumber;
    private String businessHours;
    private Double xCoordinate;
    private Double yCoordinate;
    private String roadAddress;
    private String city;
    private String district;
    private double avgRating;
    private int reviewCount; // 리뷰 개수 추가

    public BakeryDetailResponseDto(Bakery entity, int reviewCount) {
        this.bakeryId = entity.getId();
        this.name = entity.getName();
        this.bestBread = entity.getBestBread();
        this.phoneNumber = entity.getPhoneNumber();
        this.businessHours = entity.getBusinessHours();
        this.xCoordinate = entity.getXCoordinate();
        this.yCoordinate = entity.getYCoordinate();
        this.city = entity.getCity();
        this.district = entity.getDistrict();
        this.roadAddress = entity.getRoadAddress();
        this.avgRating = entity.getAvgRating();
        this.reviewCount = reviewCount;
    }
}
