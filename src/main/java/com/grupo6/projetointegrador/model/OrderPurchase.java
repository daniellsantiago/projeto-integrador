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

    @OneToOne(mappedBy = "orderPurchase", cascade = CascadeType.PERSIST)
    private Buyer buyerId;

    private LocalDate dateOrder;

    @OneToMany(mappedBy = "orderPurchase")
    private List<ProductOrder> productOrders;

    @Enumerated(EnumType.ORDINAL)
    private StatusOrder status;
}
