package com.example.breadfeet_BE.domain.favorite;

import com.example.breadfeet_BE.domain.bakery.Bakery;
import com.example.breadfeet_BE.domain.bakery.BakeryRepository;
import com.example.breadfeet_BE.domain.favorite.dto.FavoriteListResponseDto;
import com.example.breadfeet_BE.domain.favorite.dto.FavoriteResponseDto;
import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BakeryRepository bakeryRepository;

    // 즐겨찾기 추가
    public FavoriteResponseDto addFavorite(Long userId, Long bakeryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Bakery bakery = bakeryRepository.findById(bakeryId)
                .orElseThrow(() -> new NoSuchElementException("Bakery not found with id: " + bakeryId));

        // 이미 즐겨찾기 되어 있는지 확인
        if (favoriteRepository.findByUserAndBakery(user, bakery).isPresent()) {
            throw new IllegalStateException("Already favorited bakery");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .bakery(bakery)
                .build();

        Favorite savedFavorite = favoriteRepository.save(favorite);

        return new FavoriteResponseDto(savedFavorite.getId(), savedFavorite.getBakery().getId(), savedFavorite.getUser().getId());
    }

    // 즐겨찾기 삭제
    public void deleteFavorite(Long userId, Long bakeryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        Favorite favorite = favoriteRepository.findByUserAndBakeryId(user, bakeryId)
                .orElseThrow(() -> new NoSuchElementException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    // 즐겨찾기 목록 조회
    public List<FavoriteListResponseDto> getUserFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        List<Favorite> favorites = favoriteRepository.findAllByUser(user);

        return favorites.stream()
                .map(favorite -> new FavoriteListResponseDto(favorite.getBakery()))
                .collect(Collectors.toList());
    }
}
