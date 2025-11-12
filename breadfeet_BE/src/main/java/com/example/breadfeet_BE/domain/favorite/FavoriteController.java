package com.example.breadfeet_BE.domain.favorite;

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
@RequestMapping("/api/v1/bakeries")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 즐겨찾기 추가
    @PostMapping("/{bakeryId}/favorite")
    public ResponseEntity<FavoriteResponseDto> addFavorite(@PathVariable Long bakeryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        FavoriteResponseDto favoriteResponseDto = favoriteService.addFavorite(userId, bakeryId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(favoriteResponseDto);
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{bakeryId}/favorite")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long bakeryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        favoriteService.deleteFavorite(userId, bakeryId);
        return ResponseEntity.noContent().build();
    }
}
