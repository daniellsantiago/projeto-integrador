package com.grupo6.projetointegrador.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "orderPurchase", cascade = CascadeType.PERSIST)
    private Buyer buyerId;

    private LocalDate dateOrder;
    @OneToMany(mappedBy = "orderPurchase")
    private List<ProductOrder> productOrders;

    @Enumerated(EnumType.ORDINAL)
    private StatusOrder status;
}
