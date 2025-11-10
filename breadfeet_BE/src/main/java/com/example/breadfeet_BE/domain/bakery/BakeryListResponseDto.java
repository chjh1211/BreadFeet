package com.example.breadfeet_BE.domain.bakery;

import lombok.Getter;

@Getter
public class BakeryListResponseDto {
    private Long bakeryId;
    private String name;
    private String city;
    private String district;
    private String town;

    public BakeryListResponseDto(Bakery entity) {
        this.bakeryId = entity.getId();
        this.name = entity.getName();
        this.city = entity.getCity();
        this.district = entity.getDistrict();
        this.town = entity.getTown();
    }
}
