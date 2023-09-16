package com.ingemark.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 10)
    private String code;
    private String name;
    private BigDecimal priceEur;
    private BigDecimal priceUsd;
    private String description;
    private boolean isAvailable;
}
