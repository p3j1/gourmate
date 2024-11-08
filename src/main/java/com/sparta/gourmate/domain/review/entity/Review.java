package com.sparta.gourmate.domain.review.entity;

import com.sparta.gourmate.domain.order.entity.Order;
import com.sparta.gourmate.domain.store.entity.Store;
import com.sparta.gourmate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "p_reviews")
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int rating;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Review(int rating, String content, User user, Order order, Store store) {
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.order = order;
        this.store = store;
    }
}
