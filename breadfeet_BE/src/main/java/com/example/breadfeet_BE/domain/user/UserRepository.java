package com.example.breadfeet_BE.domain.user; // 본인 패키지명 확인

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // User 엔티티의 'kakaoSocialId' 필드로 사용자를 찾습니다.
    Optional<User> findByKakaoSocialId(String kakaoSocialId);
}