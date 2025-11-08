package com.example.breadfeet_BE.domain.user.entity;

import com.example.breadfeet_BE.base.BaseEntity;
import com.example.breadfeet_BE.domain.review.entity.Review;
import com.example.breadfeet_BE.domain.like.entity.LikeBakery;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // user 는 예약어라 복수형 사용
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String kakaoSocialId;

    private String profileImageUrl;

    // --- 연관 관계 ---
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeBakery> likedBakeries = new ArrayList<>();
}
