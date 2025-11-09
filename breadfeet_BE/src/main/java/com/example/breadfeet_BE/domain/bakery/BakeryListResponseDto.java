package com.example.breadfeet_BE.domain.bakery;

import lombok.Getter;

@Getter
public class BakeryListResponseDto {
    private Long bakeryId;
    private String name;
    private String addres;;

    public BakeryListResponseDto(Bakery entity) {
        this.bakeryId = entity.getBakeryId();
        this.name = entity.getName();
        this.addres = entity.getCity();
    }
}
