package com.example.breadfeet_BE.domain.review;

import com.example.breadfeet_BE.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.bakery.id = :bakeryId")
    List<Review> findAllWithUserByBakeryId(@Param("bakeryId") Long bakeryId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.bakery.id = :bakeryId")
    Double findAverageRatingByBakeryId(@Param("bakeryId") Long bakeryId);

    List<Review> findByUser_Id(Long userId);

    long countByUserAndBakery_CityAndBakery_District(User user, String city, String district);

    long countByUserAndCreatedAtAfter(User user, LocalDateTime dateTime);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user = :user AND FUNCTION('YEAR', r.createdAt) = :year AND FUNCTION('MONTH', r.createdAt) = :month")
    long countByUserAndCreatedAtYearAndMonth(@Param("user") User user, @Param("year") int year, @Param("month") int month);
}
