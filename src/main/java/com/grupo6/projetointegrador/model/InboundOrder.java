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
public class InboundOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private WarehouseOperator warehouseOperator;

    @OneToOne
    private Section section;

    @OneToMany(mappedBy = "inboundOrder")
    private List<ItemBatch> itemBatches;

    @OneToOne
    private Warehouse warehouse;

    private LocalDate orderDate;
}
