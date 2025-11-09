package com.example.breadfeet_BE.domain.user;

import com.example.breadfeet_BE.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
// import java.util.ArrayList; // Review, Like 클래스가 없으면 임시 주석 처리
// import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users") // DB에 'User' 테이블이 생성됩니다.
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String kakaoSocialId; // 'kakaoId'가 아니라 'kakaoSocialId'입니다.

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(length = 512)
    private String profileImageUrl;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(nullable = false, length = 20)
    private String role = "USER";

    // --- (추가) 소셜 로그인을 위한 Builder ---
    @Builder
    public User(String kakaoSocialId, String nickname, String profileImageUrl) {
        this.kakaoSocialId = kakaoSocialId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    // --- (추가) 사용자 정보 업데이트 ---
    public User update(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        return this;
    }

    // Review, Like 클래스가 아직 없으면 이 부분은 주석 처리하세요.
    // @OneToMany(mappedBy = "user")
    // private List<Review> reviews = new ArrayList<>();
    //
    // @OneToMany(mappedBy = "user")
    // private List<Like> likes = new ArrayList<>();
}