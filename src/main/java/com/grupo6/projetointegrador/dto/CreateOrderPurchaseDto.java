package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.Buyer;
import com.grupo6.projetointegrador.model.ProductOrder;
import com.grupo6.projetointegrador.model.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderPurchaseDto {
  private Buyer buyerId;
  private LocalDate dateOrder;
  private List<ProductOrder> productOrders;
  private StatusOrder status;
}
