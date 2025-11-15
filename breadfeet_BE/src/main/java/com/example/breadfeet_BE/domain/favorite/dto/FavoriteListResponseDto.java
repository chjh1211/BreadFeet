package com.example.breadfeet_BE.domain.favorite.dto;

import com.example.breadfeet_BE.domain.bakery.Bakery;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteListResponseDto {
    private Long bakeryId;
    private String name;
    private String address;


    public FavoriteListResponseDto(Bakery bakery) {
        this.bakeryId = bakery.getId();
        this.name = bakery.getName();
        this.address = bakery.getRoadAddress();
    }
}
