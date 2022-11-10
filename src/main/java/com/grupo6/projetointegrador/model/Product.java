package com.grupo6.projetointegrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long buyerId;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private StorageType category;

    @ManyToOne
    @JsonBackReference
    private Seller seller;
}
