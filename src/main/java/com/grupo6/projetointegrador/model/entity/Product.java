package com.grupo6.projetointegrador.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.grupo6.projetointegrador.model.enumeration.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JsonBackReference
    private Seller seller;

    // US06 Update
    private BigDecimal iteHeight;
    private BigDecimal iteLength;
    private BigDecimal iteWidth;

    public Product(long l, BigDecimal valueOf, Category fresco, Seller seller) {
    }
}
