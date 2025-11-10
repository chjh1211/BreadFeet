package com.example.breadfeet_BE.domain.menu;

import lombok.Getter;

@Getter
public class MenuResponseDto {
    private Long menuId;
    private String name;
    private int price;
    // private int reviewCount;
    // private int likeCount;

    // Menu 엔티티를 이 DTO로 변환
    public MenuResponseDto(Menu entity) {
        this.menuId = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        // this.reviewCount = entity.getReviewCount();
        // this.likeCount = entity.getLikeCount();
    }
}
