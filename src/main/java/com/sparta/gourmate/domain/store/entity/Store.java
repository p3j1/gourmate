package com.sparta.gourmate.domain.store.entity;

import com.sparta.gourmate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "p_stores")
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column
    private int averageRating;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Store(String name, String location, int averageRating, Category category, User user) {
        this.name = name;
        this.location = location;
        this.averageRating = averageRating;
        this.category = category;
        this.user = user;
    }
}
