package com.example.breadfeet_BE.domain.like.entity;

import com.example.breadfeet_BE.base.BaseEntity;
import com.example.breadfeet_BE.domain.user.entity.User;
import com.example.breadfeet_BE.domain.bakery.entity.Bakery;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "like_bakery")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeBakery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bakery_id", nullable = false)
    private Bakery bakery;
}
