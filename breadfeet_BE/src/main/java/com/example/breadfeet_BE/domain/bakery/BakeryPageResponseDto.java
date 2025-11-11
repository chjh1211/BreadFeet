package com.example.breadfeet_BE.domain.bakery;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class BakeryPageResponseDto {
    private final List<BakeryListResponseDto> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    public BakeryPageResponseDto(Page<BakeryListResponseDto> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }
}
