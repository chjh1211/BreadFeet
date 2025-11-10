package com.example.breadfeet_BE.domain.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // "GET /api/bakeries/{bakeryId}/menus"
    @GetMapping("/api/v1/bakeries/{bakeryId}/menus")
    public ResponseEntity<List<MenuResponseDto>> getMenusByBakery(
            @PathVariable Long bakeryId
            // (참고) Query: sortBy 등은 나중에 'Paging' 기능 구현 시 추가
    ) {
        List<MenuResponseDto> menuList = menuService.findMenusByBakeryId(bakeryId);
        return ResponseEntity.ok(menuList);
    }
}
