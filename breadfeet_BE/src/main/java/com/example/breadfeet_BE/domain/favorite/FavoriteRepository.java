package com.example.breadfeet_BE.domain.favorite;

import com.example.breadfeet_BE.domain.bakery.Bakery;
import com.example.breadfeet_BE.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    // 1. (즐겨찾기 추가 시) 사용자와 빵집 기준으로 이미 즐겨찾기했는지 확인
    Optional<Favorite> findByUserAndBakery(User user, Bakery bakery);

    // 2. (즐겨찾기 삭제 시) 사용자와 빵집 ID로 즐겨찾기 정보 찾기
    //    (N+1 방지를 위해 User 객체와 빵집 ID를 함께 사용)
    Optional<Favorite> findByUserAndBakeryId(User user, Long bakeryId);

    // 3. (즐겨찾기 목록 조회 시) 사용자로 즐겨찾기 목록 조회
    List<Favorite> findAllByUser(User user);
}
