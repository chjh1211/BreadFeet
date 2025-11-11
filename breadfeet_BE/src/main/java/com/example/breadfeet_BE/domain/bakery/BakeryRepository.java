package com.example.breadfeet_BE.domain.bakery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BakeryRepository extends JpaRepository<Bakery, Long> {
    Page<Bakery> findByNameContaining(String name, Pageable pageable);
}
