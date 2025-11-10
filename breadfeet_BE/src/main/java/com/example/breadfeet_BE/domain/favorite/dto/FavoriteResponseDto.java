package com.example.breadfeet_BE.domain.favorite.dto;

import lombok.Getter;

@Getter
public class FavoriteResponseDto {
    private final Long favoriteId;
    private final Long bakeryId;
    private final Long userId;

    public FavoriteResponseDto(Long favoriteId, Long bakeryId, Long userId) {
        this.favoriteId = favoriteId;
        this.bakeryId = bakeryId;
        this.userId = userId;
    }
}
