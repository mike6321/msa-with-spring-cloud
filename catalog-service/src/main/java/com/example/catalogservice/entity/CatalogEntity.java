package com.example.catalogservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "catalog")
public class CatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id", nullable = false, length = 120, unique = true)
    private String productId;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "stock", nullable = false)
    private Integer stock;
    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

}
