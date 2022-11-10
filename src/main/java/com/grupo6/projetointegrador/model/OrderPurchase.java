package com.grupo6.projetointegrador.model;

import lombok.*;

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

    @OneToMany(mappedBy = "orderPurchase")
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
