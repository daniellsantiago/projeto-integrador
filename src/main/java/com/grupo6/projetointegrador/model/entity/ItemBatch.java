package com.grupo6.projetointegrador.model.entity;

import com.grupo6.projetointegrador.model.enumeration.StorageType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Product product;

    private int productQuantity;

    private LocalDate manufacturingDate;

    private LocalDateTime manufacturingTime;

    private Long volume;

    private LocalDate dueDate;

    private BigDecimal price;

    @ManyToOne
    private InboundOrder inboundOrder;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    public ItemBatch(Product product, int productQuantity, LocalDate manufacturingDate, LocalDateTime manufacturingTime, Long volume, LocalDate dueDate, BigDecimal price, InboundOrder inboundOrder, StorageType storageType) {
        this.product = product;
        this.productQuantity = productQuantity;
        this.manufacturingDate = manufacturingDate;
        this.manufacturingTime = manufacturingTime;
        this.volume = volume;
        this.dueDate = dueDate;
        this.price = price;
        this.inboundOrder = inboundOrder;
        this.storageType = storageType;
    }
}
