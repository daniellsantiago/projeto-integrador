package com.grupo6.projetointegrador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Buyer buyer;

    private LocalDate dateOrder;

    @OneToMany(mappedBy = "orderPurchase", cascade = CascadeType.PERSIST)
    private List<ProductOrder> productOrders;

    @Enumerated(EnumType.ORDINAL)
    private StatusOrder status;

    public OrderPurchase(Buyer buyer, LocalDate dateOrder, List<ProductOrder> productOrder, StatusOrder status) {
        this.buyer = buyer;
        this.dateOrder = dateOrder;
        this.productOrders = productOrder;
        this.status = status;
    }
}
