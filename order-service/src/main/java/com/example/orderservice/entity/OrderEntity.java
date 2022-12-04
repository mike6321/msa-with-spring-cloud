package com.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id", nullable = false, length = 120, unique = true)
    private String productId;
    @Column(name = "qty", nullable = false)
    private Integer qty;
    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

}
