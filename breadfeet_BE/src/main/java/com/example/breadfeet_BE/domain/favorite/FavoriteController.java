package com.example.breadfeet_BE.domain.favorite;

import com.example.breadfeet_BE.domain.favorite.dto.FavoriteRequestDto;
import com.example.breadfeet_BE.domain.favorite.dto.FavoriteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 즐겨찾기 추가
    @PostMapping
    public ResponseEntity<Map<String,String>> addFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        FavoriteResponseDto favoriteResponseDto = favoriteService.addFavorite(userId, favoriteRequestDto.getBakeryId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "추가 완료"));
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{bakeryId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long bakeryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        favoriteService.deleteFavorite(userId, bakeryId);
        return ResponseEntity.noContent().build();
    }
}
