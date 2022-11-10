package com.grupo6.projetointegrador.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class InboundOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private WarehouseOperator warehouseOperator;

    @OneToOne
    private Section section;

    @OneToMany(mappedBy = "inboundOrder", cascade = CascadeType.PERSIST)
    private List<ItemBatch> itemBatches;

    @OneToOne
    private Warehouse warehouse;

    private LocalDate orderDate;


}
