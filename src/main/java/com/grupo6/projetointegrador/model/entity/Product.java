package com.grupo6.projetointegrador.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.grupo6.projetointegrador.model.enumeration.Category;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.*;

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
}
