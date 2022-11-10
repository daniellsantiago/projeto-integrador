package com.grupo6.projetointegrador.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
<<<<<<< HEAD:src/main/java/com/grupo6/projetointegrador/model/entity/Product.java
import com.grupo6.projetointegrador.model.enumeration.Category;
=======
>>>>>>> refs/remotes/origin/feature/freshproductslist:src/main/java/com/grupo6/projetointegrador/model/Product.java
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

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JsonBackReference
    private Seller seller;
}
