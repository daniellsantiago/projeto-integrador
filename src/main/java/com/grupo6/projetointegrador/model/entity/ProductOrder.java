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

    @OneToOne
    private Product product;

    private int quantity;

    public ProductOrder(OrderPurchase orderPurchase, Product product, int quantity) {
        this.orderPurchase = orderPurchase;
        this.product = product;
        this.quantity = quantity;
    }
}

