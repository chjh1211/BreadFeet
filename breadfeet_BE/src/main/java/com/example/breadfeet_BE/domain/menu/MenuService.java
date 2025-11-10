package com.example.breadfeet_BE.domain.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public List<MenuResponseDto> findMenusByBakeryId(Long bakeryId) {

        // 1. Repository에게 빵집 ID로 모든 메뉴를 찾아오라고 시킴
        return menuRepository.findAllByBakery_Id(bakeryId)
                .stream()
                // 2. 각 Menu 엔티티를 DTO로 변환
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());
    }
}
