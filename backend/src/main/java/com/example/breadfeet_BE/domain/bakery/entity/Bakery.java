package com.example.breadfeet_BE.domain.bakery.entity;

import com.example.breadfeet_BE.base.BaseEntity;
import com.example.breadfeet_BE.domain.review.entity.Review;
import com.example.breadfeet_BE.domain.like.entity.LikeBakery;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bakery")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bakery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bakeryId;

    @Column(nullable = false)
    private String name; // 빵집 이름

    private String bestBread;
    private String phoneNumber;
    private String businessHours;

    private Double xCoordinate;
    private Double yCoordinate;

    private String city;       // 시, 광역시
    private String district;   // 구, 군
    private String town;       // 읍, 면, 동

    // --- 연관 관계 ---
    @OneToMany(mappedBy = "bakery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "bakery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeBakery> likes = new ArrayList<>();
}
