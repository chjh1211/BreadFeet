package com.example.breadfeet_BE.domain.favorite;

import com.example.breadfeet_BE.domain.bakery.Bakery;
import com.example.breadfeet_BE.domain.base.BaseEntity;
import com.example.breadfeet_BE.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "favorite")
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    // 이 '즐겨찾기'를 등록한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 이 '즐겨찾기'의 대상 빵집
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bakery_id", nullable = false)
    private Bakery bakery;

    // --- 생성자 (Builder) ---
    @Builder
    public Favorite(User user, Bakery bakery) {
        this.user = user;
        this.bakery = bakery;
    }
}
