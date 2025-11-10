package com.example.breadfeet_BE.domain.bakery;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BakeryRepository extends JpaRepository<Bakery, Long> {
    List<Bakery> findByNameContaining(String name);
}
