package com.grupo6.projetointegrador.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private OrderPurchase orderPurchase;

    private Long productId;

    private int quantity;

    public ProductOrder(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}

