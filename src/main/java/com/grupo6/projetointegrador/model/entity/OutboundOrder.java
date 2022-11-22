package com.grupo6.projetointegrador.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutboundOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private WarehouseOperator warehouseOperator;

  @OneToOne
  private Section section;

  @OneToMany(
          mappedBy = "outboundOrder",
          cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
  )
  private List<OutboundItemBatch> outboundItemBatches;

  @OneToOne
  private Warehouse warehouse;

  private LocalDate orderDate;
}