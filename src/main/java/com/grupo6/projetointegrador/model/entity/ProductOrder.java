package com.grupo6.projetointegrador.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderPurchase orderPurchase;

    private Long productId;

    private int quantity;

    public ProductOrder(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public ProductOrder(OrderPurchase orderPurchase, Long productId, int quantity) {
        this.orderPurchase = orderPurchase;
        this.productId = productId;
        this.quantity = quantity;
    }
}

