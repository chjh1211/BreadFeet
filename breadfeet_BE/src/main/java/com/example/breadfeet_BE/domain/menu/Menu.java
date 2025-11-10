package com.example.breadfeet_BE.domain.menu;

import com.example.breadfeet_BE.domain.bakery.Bakery;
import com.example.breadfeet_BE.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "menu")
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bakery_id")
    private Bakery bakery;
}
