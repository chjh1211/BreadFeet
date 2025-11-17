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
    private String city;
    private String district;
    private String roadAddress;
    private double avgRating;

    public BakeryDetailResponseDto(Bakery entity) {
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
    }
}
