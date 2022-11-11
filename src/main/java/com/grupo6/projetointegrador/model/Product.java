package com.grupo6.projetointegrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JsonBackReference
    private Seller seller;

    @Enumerated(EnumType.STRING)
    private StorageType category;
}
