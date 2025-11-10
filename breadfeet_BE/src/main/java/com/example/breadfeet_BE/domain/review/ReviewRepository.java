package com.example.breadfeet_BE.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.bakery.Id = :bakeryId")
    List<Review> findAllWithUserByBakeryId(@Param("bakeryId") Long bakeryId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.bakery.Id = :bakeryId")
    Double findAverageRatingByBakeryId(@Param("bakeryId") Long bakeryId);

    List<Review> findByUser_Id(Long userId); // Add this method
}
