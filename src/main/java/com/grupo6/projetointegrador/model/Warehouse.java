package com.grupo6.projetointegrador.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "warehouse")
    private List<Section> sections;

    @OneToOne
    private WarehouseOperator warehouseOperator;
}
