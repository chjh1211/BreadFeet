package com.example.breadfeet_BE.domain.bakery;

import com.example.breadfeet_BE.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bakery")
public class Bakery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bakery_id")
    private Long id;

    @Column(nullable = false)
    private String name;                   // 빵집 이름

    private String bestBread;
    private String phoneNumber;
    private String businessHours;

    private Double xCoordinate;
    private Double yCoordinate;

    private String roadAddress;       // 읍, 면, 동
    private String city;       // 시, 광역시
    private String district;   // 구, 군

    @Transient
    @Setter
    private double avgRating;
}
