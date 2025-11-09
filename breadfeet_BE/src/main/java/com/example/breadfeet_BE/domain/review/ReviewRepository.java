package com.example.breadfeet_BE.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByBakeryId(Long bakeryId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.bakery.Id = :bakeryId")
    Double findAverageRatingByBakeryId(@Param("bakeryId") Long bakeryId);
}
