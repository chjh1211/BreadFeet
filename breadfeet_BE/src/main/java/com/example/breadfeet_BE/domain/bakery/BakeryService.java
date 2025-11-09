package com.example.breadfeet_BE.domain.bakery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BakeryService {
    private final BakeryRepository bakeryRepository;

    @Transactional(readOnly = true)
    public List<BakeryListResponseDto> findAllBakeries(String searchQuery){
        List<Bakery> bakeries;

        if (searchQuery == null || searchQuery.isBlank()){
            bakeries = bakeryRepository.findAll();
        }
        else{
            bakeries = bakeryRepository.findByNameContaining(searchQuery);
        }

        return bakeries.stream()
                .map(BakeryListResponseDto::new)
                .collect(Collectors.toList());
    }


}
