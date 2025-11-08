package com.example.breadfeet_BE.domain.review.entity;

import com.example.breadfeet_BE.base.BaseEntity;
import com.example.breadfeet_BE.domain.user.entity.User;
import com.example.breadfeet_BE.domain.bakery.entity.Bakery;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bakery_id", nullable = false)
    private Bakery bakery;

    private Double rating;

    @Column(length = 1000)
    private String content;
}
