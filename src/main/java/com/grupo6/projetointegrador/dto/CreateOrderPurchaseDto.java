package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.Buyer;
import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.entity.ProductOrder;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderPurchaseDto {
  private Long buyer;
  private LocalDate dateOrder;
  private StatusOrder status;
  private List<ProductOrderDto> productOrders;
  public static OrderPurchase toOrderPurchase(CreateOrderPurchaseDto createdOrderPurchase, List<ProductOrder> productOrder, Buyer buyer) {
    return new OrderPurchase(
            buyer,
            createdOrderPurchase.getDateOrder(),
            productOrder,
            createdOrderPurchase.getStatus()   );
  }
}
