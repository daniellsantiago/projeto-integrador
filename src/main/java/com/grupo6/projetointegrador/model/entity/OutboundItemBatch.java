package com.grupo6.projetointegrador.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grupo6.projetointegrador.model.enumeration.Category;
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
public class OutboundItemBatch {
  @Id
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
  @JsonIgnore
  private OutboundOrder outboundOrder;

  @ManyToOne
  @JsonIgnore
  private InboundOrder inboundOrder;

  @Enumerated(EnumType.STRING)
  private Category category;

  public OutboundItemBatch(Long id, Product product, int productQuantity, LocalDate manufacturingDate,
                           LocalDateTime manufacturingTime, Long volume, LocalDate dueDate, BigDecimal price, Category category, OutboundOrder outboundOrder, InboundOrder inboundOrder) {
    this.id = id;
    this.product = product;
    this.productQuantity = productQuantity;
    this.manufacturingDate = manufacturingDate;
    this.manufacturingTime = manufacturingTime;
    this.volume = volume;
    this.dueDate = dueDate;
    this.price = price;
    this.category = category;
  }

  public OutboundItemBatch(ItemBatch itemBatch, InboundOrder inboundOrder, OutboundOrder outboundOrder) {
    this.id = itemBatch.getId();
    this.product = itemBatch.getProduct();
    this.productQuantity = itemBatch.getProductQuantity();
    this.manufacturingDate = itemBatch.getManufacturingDate();
    this.manufacturingTime = itemBatch.getManufacturingTime();
    this.volume = itemBatch.getVolume();
    this.dueDate = itemBatch.getDueDate();
    this.price = itemBatch.getPrice();
    this.inboundOrder = inboundOrder;
    this.outboundOrder = outboundOrder;
    this.category = itemBatch.getCategory();
  }
}