package com.grupo6.projetointegrador.model.entity;

import com.grupo6.projetointegrador.model.enumeration.RefundReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPurchaseRefund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private OrderPurchase orderPurchase;

    @Enumerated(EnumType.STRING)
    private RefundReason reason;

    private LocalDate refundDate;
}
